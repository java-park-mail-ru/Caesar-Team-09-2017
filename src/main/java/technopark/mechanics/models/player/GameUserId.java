package technopark.mechanics.models.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import technopark.mechanics.models.Snap;
import technopark.model.account.dao.AccountDao;
import technopark.model.id.Id;


public class GameUserId extends GameObject {
    @Nullable
    private Id<AccountDao> gameUserId;

    @Nullable
    public Id<AccountDao> getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(@Nullable Id<AccountDao> gameUserId) {
        this.gameUserId = gameUserId;
    }

    @Override
    @NotNull
    public GameUserIdSnap getSnap() {
        return new GameUserIdSnap(this);
    }

    public static final class GameUserIdSnap implements Snap<GameUserId> {

        @Nullable
        private final Id<AccountDao> occupant;

        public GameUserIdSnap(@NotNull GameUserId gameUserId) {
            this.occupant = gameUserId.gameUserId;
        }

        @Nullable
        public Id<AccountDao> getOccupant() {
            return occupant;
        }
    }

    @Override
    public String toString() {
        return "GameUserId{"
                + "gameUserId=" + gameUserId
                + '}';
    }
}
