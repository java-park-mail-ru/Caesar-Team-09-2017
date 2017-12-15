package technopark.mechanics.services.session;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import technopark.mechanics.Config;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.models.session.GameTaskScheduler;
import technopark.mechanics.services.GameInitService;
import technopark.mechanics.services.snap.ClientSnapshotsService;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;
import technopark.websocket.RemotePointService;

import java.util.*;

@Service
public class  GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);
    @NotNull
    private final Map<Id<AccountDao>, GameSession> usersMap = new HashMap<>();
    @NotNull
    private final Set<GameSession> gameSessions = new LinkedHashSet<>();

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final MechanicsTimeService timeService;

    @NotNull
    private final GameInitService gameInitService;

    @NotNull
    private final GameTaskScheduler gameTaskScheduler;

    @NotNull
    private final ClientSnapshotsService clientSnapshotsService;

    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull MechanicsTimeService timeService,
                              @NotNull GameInitService gameInitService,
                              @NotNull GameTaskScheduler gameTaskScheduler,
                              @NotNull ClientSnapshotsService clientSnapshotsService) {
        this.remotePointService = remotePointService;
        this.timeService = timeService;
        this.gameInitService = gameInitService;
        this.gameTaskScheduler = gameTaskScheduler;
        this.clientSnapshotsService = clientSnapshotsService;
    }

    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    @Nullable
    public GameSession getSessionForUser(@NotNull Id<AccountDao> userId) {
        return usersMap.get(userId);
    }

    public boolean isPlaying(@NotNull Id<AccountDao> userId) {
        return usersMap.containsKey(userId);
    }

    public void forceTerminate(@NotNull GameSession gameSession, boolean error) {
        final boolean exists = gameSessions.remove(gameSession);
        gameSession.setFinished();
        boolean singlePlay = gameSession.isSinglePlay();
        usersMap.remove(gameSession.getFirst().getAccountId());
        if (!singlePlay) {
            usersMap.remove(gameSession.getSecond().getAccountId());
        }
        @SuppressWarnings("AvoidInlineConditionals")
        final CloseStatus status;
        if (error) {
            status = CloseStatus.SERVER_ERROR;
        } else {
            status = CloseStatus.NORMAL;
        }

        if (exists) {
            remotePointService.cutDownConnection(gameSession.getFirst().getAccountId(), status);
            if (!singlePlay) {
                remotePointService.cutDownConnection(gameSession.getSecond().getAccountId(), status);
            }
        }
        clientSnapshotsService.clearForUser(gameSession.getFirst().getAccountId());
        if (!singlePlay) {
            clientSnapshotsService.clearForUser(gameSession.getSecond().getAccountId());
        }

        StringBuilder stringBuilder = new StringBuilder("Game session " + gameSession.getSessionId());
        if (error) {
            stringBuilder.append(" was terminated due to error. ");
        } else {
            stringBuilder.append(" was cleaned. ");
        }
        LOGGER.info(stringBuilder.toString());
    }

    public boolean checkHealthState(@NotNull GameSession gameSession) {
        return gameSession.getPlayers().stream().map(GameUser::getAccountId).allMatch(remotePointService::isConnected);
    }

    public void startGame(@NotNull AccountDao first, @Nullable AccountDao second) {
        final GameSession gameSession = new GameSession(first, second, this, timeService);
        gameSessions.add(gameSession);
        usersMap.put(gameSession.getFirst().getAccountId(), gameSession);
        if (!gameSession.isSinglePlay()) {
            usersMap.put(gameSession.getSecond().getAccountId(), gameSession);
        }
        gameInitService.initGameFor(gameSession);
        gameTaskScheduler.schedule(Config.START_SWITCH_DELAY, new SwapTask(gameSession, gameTaskScheduler, Config.START_SWITCH_DELAY));
        LOGGER.info("Game session " + gameSession.getSessionId() + " started. " + gameSession.toString());
    }

    public void finishGame(@NotNull GameSession gameSession) {
        gameSession.setFinished();
/*        final FinishGame.Overcome firstOvercome;
        final FinishGame.Overcome secondOvercome;
        final int firstScore = gameSession.getFirst().claimPart(MechanicPart.class).getEnergy();
        final int secondScore = gameSession.getSecond().claimPart(MechanicPart.class).getEnergy();
        if (firstScore == secondScore) {
            firstOvercome = FinishGame.Overcome.DRAW;
            secondOvercome = FinishGame.Overcome.DRAW;
        } else if (firstScore > secondScore) {
            firstOvercome = FinishGame.Overcome.WIN;
            secondOvercome = FinishGame.Overcome.LOSE;
        } else {
            firstOvercome = FinishGame.Overcome.LOSE;
            secondOvercome = FinishGame.Overcome.WIN;
        }

        try {
            remotePointService.sendMessageToUser(gameSession.getFirst().getAccountId(), new FinishGame(firstOvercome));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getFirst().getAccountDao().getUsername()), ex);
        }

        try {
            remotePointService.sendMessageToUser(gameSession.getSecond().getAccountId(), new FinishGame(secondOvercome));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getSecond().getAccountDao().getUsername()), ex);
        }*/
    }

    private static final class SwapTask extends GameTaskScheduler.GameSessionTask {

        private final GameTaskScheduler gameTaskScheduler;
        private final long currentDelay;

        private SwapTask(GameSession gameSession, GameTaskScheduler gameTaskScheduler, long currentDelay) {
            super(gameSession);
            this.gameTaskScheduler = gameTaskScheduler;
            this.currentDelay = currentDelay;
        }

        @Override
        public void operate() {
            if (getGameSession().isFinished()) {
                return;
            }
            final long newDelay = Math.max(currentDelay - Config.SWITCH_DELTA, Config.SWITCH_DELAY_MIN);
            gameTaskScheduler.schedule(newDelay,
                    new SwapTask(getGameSession(), gameTaskScheduler, newDelay));
        }
    }

}
