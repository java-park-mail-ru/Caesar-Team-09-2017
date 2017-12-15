package technopark.mechanics.models.session;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import technopark.mechanics.services.session.GameSessionService;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.MapForGame;
import technopark.mechanics.models.player.GameUser;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;
    private boolean singlePlay;

    @NotNull
    private final Id<GameSession> sessionId;
    @NotNull
    private final GameUser first;
    @Nullable
    private final GameUser second;
    @NotNull
    private final MapForGame mapForGame;
    @NotNull
    private final GameSessionService gameSessionService;

    public GameSession(@NotNull AccountDao user1,
                       @Nullable AccountDao user2,
                       @NotNull GameSessionService gameSessionService,
                       @NotNull MechanicsTimeService mechanicsTimeService) {
        this.gameSessionService = gameSessionService;
        this.sessionId = Id.of(ID_GENERATOR.getAndIncrement());
        this.first = new GameUser(user1, mechanicsTimeService);
        if (user2 != null) {
            this.second =  new GameUser(user2, mechanicsTimeService);
            this.singlePlay = false;

        } else {
            this.second =  null;
            this.singlePlay = true;
        }
        this.isFinished = false;

        this.mapForGame = new MapForGame(this);
    }

    @NotNull
    public Id<GameSession> getSessionId() {
        return sessionId;
    }

    public GameUser getEnemy(@NotNull Id<AccountDao> userId) {
        if (second == null) {
            return null;
        }

        if (userId.equals(first.getAccountId())) {
            return second;
        }
        if (userId.equals(second.getAccountId())) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for models but user not participant");
    }

    @NotNull
    public MapForGame getMapForGame() {
        return mapForGame;
    }

    @NotNull
    public GameUser getUser(int indexOfUser) {
        if (indexOfUser == 0) {
            return first;
        } else {
            return second;
        }
    }

    @NotNull
    public GameUser getFirst() {
        return first;
    }

    public GameUser getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final GameSession another = (GameSession) object;
        return sessionId.equals(another.sessionId);
    }

    @NotNull
    public List<GameUser> getPlayers() {
        if (second == null) {
            return Arrays.asList(first);
        }
        return Arrays.asList(first, second);
    }

    public boolean isSinglePlay() {
        return singlePlay;
    }

    public void setSinglePlay(boolean singlePlay) {
        this.singlePlay = singlePlay;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }

    public void terminateSession() {
        gameSessionService.forceTerminate(this, true);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return '['
                + "sessionId=" + sessionId
                + ", first=" + first
                + ", second=" + second
                + ']';
    }

    public boolean tryFinishGame() {
        // if (first.claimPart(MechanicPart.class).getEnergy() >= Config.SCORES_TO_WIN
        //        || second.claimPart(MechanicPart.class).getEnergy() >= Config.SCORES_TO_WIN) {
        //    gameSessionService.finishGame(this);
        //    isFinished = true;
        //    return true;
        // }
        return false;
    }

    @NotNull
    public GameUser getPlayer() {
        return first;
    }
}
