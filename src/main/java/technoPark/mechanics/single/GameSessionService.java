package technoPark.mechanics.single;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import technoPark.mechanics.*;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;
import technoPark.websocket.RemotePointService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
public class GameSessionService {
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
    private final technoPark.mechanics.single.GameInitService gameInitService;

    @NotNull
    private final GameTaskScheduler gameTaskScheduler;

    @NotNull
    private final ClientSnapshotsService clientSnapshotsService;



    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull MechanicsTimeService timeService,
                              @NotNull technoPark.mechanics.single.GameInitService gameInitService,
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
        usersMap.remove(gameSession.getPlayer().getAccountId());
        final CloseStatus status = error ? CloseStatus.SERVER_ERROR : CloseStatus.NORMAL;
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getPlayer().getAccountId(), status);
        }
        clientSnapshotsService.clearForUser(gameSession.getPlayer().getAccountId());

        LOGGER.info("Game session " + gameSession.getSessionId() + (error ? " was terminated due to error. " : " was cleaned. ")
                + gameSession.toString());
    }

    public boolean checkHealthState(@NotNull GameSession gameSession) {
        return remotePointService.isConnected(gameSession.getPlayer().getAccountId());
    }

    public void startGame(@NotNull AccountDao accountDao) {
        final GameSession gameSession = new GameSession(accountDao, this, timeService);
        gameSessions.add(gameSession);
        usersMap.put(gameSession.getPlayer().getAccountId(), gameSession);
        gameInitService.initGameFor(gameSession);
        gameTaskScheduler.schedule(Config.START_SWITCH_DELAY, new SwapTask(gameSession, gameTaskScheduler, Config.START_SWITCH_DELAY));
        LOGGER.info("Game session " + gameSession.getSessionId() + " started. " + gameSession.toString());
    }

    public void finishGame(@NotNull GameSession gameSession) {
        gameSession.setFinished();
//        final FinishGame.Overcome firstOvercome;
//        final FinishGame.Overcome secondOvercome;
//        final int firstScore = gameSession.getPlayer().claimPart(MechanicPart.class).getScore();
//
//        try {
//            remotePointService.sendMessageToUser(gameSession.getPlayer().getAccountId(), new FinishGame(firstOvercome));
//        } catch (IOException ex) {
//            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
//                    gameSession.getPlayer().getAccountDao().getUsername()), ex);
//        }

    }

    private static final class SwapTask extends GameTaskScheduler.GameSessionTask {

        private final GameTaskScheduler gameTaskScheduler;
        private final long currentDelay;

        private SwapTask(technoPark.mechanics.GameSession gameSession, GameTaskScheduler gameTaskScheduler, long currentDelay) {
            super(gameSession);
            this.gameTaskScheduler = gameTaskScheduler;
            this.currentDelay = currentDelay;
        }

        @Override
        public void operate() {
            if (getGameSession().isFinished()) {
                return;
            }
//            getGameSession().getBoard().randomSwap();
            final long newDelay = Math.max(currentDelay - Config.SWITCH_DELTA, Config.SWITCH_DELAY_MIN);
            gameTaskScheduler.schedule(newDelay,
                    new SwapTask(getGameSession(), gameTaskScheduler, newDelay));
        }
    }

}
