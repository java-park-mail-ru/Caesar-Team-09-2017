package technoPark.mechanics.single;

import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.Config;
import technoPark.mechanics.MechanicsTimeService;
import technoPark.mechanics.models.GameUser;
import technoPark.mechanics.models.MechanicPart;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;

import java.util.concurrent.atomic.AtomicLong;

public class GameSession extends technoPark.mechanics.GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;

    @NotNull
    private final Id<GameSession> sessionId;
    @NotNull
    private final GameUser gameUser;
//    @NotNull
//    private final Board board;
    @NotNull
    private final GameSessionService gameSessionService;

    public GameSession(@NotNull AccountDao accountDao,
                       @NotNull GameSessionService gameSessionService,
                       @NotNull MechanicsTimeService mechanicsTimeService) {
        this.gameSessionService = gameSessionService;
        this.sessionId = Id.of(ID_GENERATOR.getAndIncrement());
        this.gameUser = new GameUser(accountDao, mechanicsTimeService);
        this.isFinished = false;
//        this.board = new Board(this);
    }

    @NotNull
    public Id<GameSession> getSessionId() {
        return sessionId;
    }

//    @NotNull
//    public Board getBoard() {
//        return board;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession another = (GameSession) o;

        return sessionId.equals(another.sessionId);
    }

    @Override
    @NotNull
    public GameUser getPlayer() {
        return gameUser;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }

    @Override
    public void terminateSession() {
        gameSessionService.forceTerminate(this, true);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return '[' +
                "sessionId=" + sessionId +
                ", gameUser=" + gameUser +
                ']';
    }

    public boolean tryFinishGame() {
        if (gameUser.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN) {
            gameSessionService.finishGame(this);
            isFinished = true;
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public GameUser getFirst() {
        return gameUser;
    }
    @Override
    @NotNull
    public GameUser getSecond() {
        return gameUser;
    }

    @Override
    public GameUser getEnemy(@NotNull Id<AccountDao> userId) {
       return gameUser;
    }

}
