package technoPark.mechanics.multi;

import technoPark.mechanics.Config;
import technoPark.mechanics.MechanicsTimeService;
import technoPark.mechanics.models.GameUser;
import technoPark.mechanics.models.MechanicPart;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;
import org.jetbrains.annotations.NotNull;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession extends technoPark.mechanics.GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;

    @NotNull
    private final Id<GameSession> sessionId;
    @NotNull
    private final GameUser first;
    @NotNull
    private final GameUser second;
//    @NotNull
//    private final Board board;
    @NotNull
    private final GameSessionService gameSessionService;

    public GameSession(@NotNull AccountDao user1,
                       @NotNull AccountDao user2,
                       @NotNull GameSessionService gameSessionService,
                       @NotNull MechanicsTimeService mechanicsTimeService) {
        this.gameSessionService = gameSessionService;
        this.sessionId = Id.of(ID_GENERATOR.getAndIncrement());
        this.first = new GameUser(user1, mechanicsTimeService);
        this.second =  new GameUser(user2, mechanicsTimeService);
        this.isFinished = false;
//        this.board = new Board(this);
    }

    @NotNull
    public Id<GameSession> getSessionId() {
        return sessionId;
    }

    @Override
    public GameUser getEnemy(@NotNull Id<AccountDao> userId) {
        if (userId.equals(first.getAccountId())) {
            return second;
        }
        if (userId.equals(second.getAccountId())) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for models but user not participant");
    }

//    @NotNull
//    public Board getBoard() {
//        return board;
//    }
    @Override
    @NotNull
    public GameUser getFirst() {
        return first;
    }
    @Override
    @NotNull
    public GameUser getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession another = (GameSession) o;

        return sessionId.equals(another.sessionId);
    }

    @NotNull
    public List<GameUser> getPlayers() {
        return Arrays.asList(first, second);
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
                ", first=" + first +
                ", second=" + second +
                ']';
    }

    public boolean tryFinishGame() {
        if (first.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN
                || second.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN) {
            gameSessionService.finishGame(this);
            isFinished = true;
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public GameUser getPlayer() {
        return first;
    }
}
