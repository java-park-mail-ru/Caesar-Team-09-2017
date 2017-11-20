package technopark.mechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.models.session.GameTaskScheduler;
import technopark.mechanics.requests.ClientSnap;
import technopark.mechanics.requests.JoinGame;
import technopark.mechanics.services.snap.ClientSnapshotsService;
import technopark.mechanics.services.session.GameSessionService;
import technopark.mechanics.services.snap.ServerSnapshotService;
import technopark.model.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;
import technopark.services.AccountService;
import technopark.websocket.RemotePointService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanicsImpl implements GameMechanics {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanicsImpl.class);

    @NotNull
    private final AccountService accountService;

    @NotNull
    private final ClientSnapshotsService clientSnapshotsService;

    @NotNull
    private final ServerSnapshotService serverSnapshotService;

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final GameSessionService gameSessionService;

    @NotNull
    private final MechanicsTimeService timeService;

    @NotNull
    private final GameTaskScheduler gameTaskScheduler;

    @NotNull
    private ConcurrentLinkedQueue<Id<AccountDao>> waiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private ConcurrentLinkedQueue<Id<AccountDao>> singlePlayers = new ConcurrentLinkedQueue<>();

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanicsImpl(@NotNull AccountService accountService,
                             @NotNull ClientSnapshotsService clientSnapshotsService,
                             @NotNull ServerSnapshotService serverSnapshotService,
                             @NotNull RemotePointService remotePointService,
                             @NotNull GameSessionService gameSessionService,
                             @NotNull MechanicsTimeService timeService,
                             @NotNull GameTaskScheduler gameTaskScheduler) {
        this.accountService = accountService;
        this.clientSnapshotsService = clientSnapshotsService;
        this.serverSnapshotService = serverSnapshotService;
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
        this.timeService = timeService;
        this.gameTaskScheduler = gameTaskScheduler;
    }

    // действие срабатываемое по приему сообщения snap от клиента
    @Override
    public void addClientSnapshot(@NotNull Id<AccountDao> userId, @NotNull ClientSnap clientSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, clientSnap));
    }

    // действие срабатываемое при подключение пользователя(когда он отправляет сообщение)
    @Override
    public void addUser(@NotNull Id<AccountDao> userId, @NotNull JoinGame joinGame) {
        if (gameSessionService.isPlaying(userId)) {
            return;
        }

        if (joinGame.getTypeOfGame().equals("multi")) {
            waiters.add(userId);
            if (LOGGER.isDebugEnabled()) {
                final AccountDao accountDao = accountService.getAccountFromId(userId.getId());
                LOGGER.debug(String.format("User %s added to the waiting list", accountDao.getUsername()));
            }
        } else {
            singlePlayers.add(userId);
            final AccountDao accountDao = accountService.getAccountFromId(userId.getId());
            LOGGER.info(String.format("User %s added to the single models", accountDao.getUsername()));
        }
    }

    private void tryStartGames() {
        final Set<AccountDao> matchedPlayers = new LinkedHashSet<>();

        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final Id<AccountDao> candidate = waiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(accountService.getAccountFromId(candidate.getId()));
            if (matchedPlayers.size() == 2) {
                final Iterator<AccountDao> iterator = matchedPlayers.iterator();
                gameSessionService.startGame(iterator.next(), iterator.next());
                matchedPlayers.clear();
            }
        }

        matchedPlayers.stream().map(AccountDao::getId).forEach(waiters::add);

        while (singlePlayers.size() >= 1) {
            final Id<AccountDao> candidate = singlePlayers.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            gameSessionService.startGame(accountService.getAccountFromId(candidate.getId()), null);
        }

    }

    private boolean insureCandidate(@NotNull Id<AccountDao> candidate) {
        return remotePointService.isConnected(candidate)
                && accountService.getAccountFromId(candidate.getId()) != null;
    }

    @Override
    public void gmStep(long frameTime) {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Can't handle models task", ex);
                }
            }
        }

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }

        gameTaskScheduler.tick();

        final List<GameSession> sessionsToTerminate = new ArrayList<>();
        final List<GameSession> sessionsToFinish = new ArrayList<>();
        for (GameSession session : gameSessionService.getSessions()) {
            if (session.tryFinishGame()) {
                sessionsToFinish.add(session);
                continue;
            }

            if (!gameSessionService.checkHealthState(session)) {
                sessionsToTerminate.add(session);
                continue;
            }

            try {
                serverSnapshotService.sendSnapshotsFor(session, frameTime);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed to send snapshots, terminating the session", ex);
                sessionsToTerminate.add(session);
            }
            session.getPlayers().forEach(user -> user.claimPart(MechanicPart.class).setDrill(false));
        }

        sessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));
        sessionsToFinish.forEach(session -> gameSessionService.forceTerminate(session, false));

        tryStartGames();
        clientSnapshotsService.reset();
        timeService.tick(frameTime);
    }

    @Override
    public void reset() {
        for (GameSession session : gameSessionService.getSessions()) {
            gameSessionService.forceTerminate(session, true);
        }
        waiters.forEach(user -> remotePointService.cutDownConnection(user, CloseStatus.SERVER_ERROR));
        waiters.clear();
        tasks.clear();
        clientSnapshotsService.reset();
        timeService.reset();
        gameTaskScheduler.reset();
    }
}
