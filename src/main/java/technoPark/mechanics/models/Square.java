package  technoPark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;


public class Square extends GameObject {
    @Nullable
    private Id<AccountDao> occupant;

    @Nullable
    public Id<AccountDao> getOccupant() {
        return occupant;
    }

    public void setOccupant(@Nullable Id<AccountDao> occupant) {
        this.occupant = occupant;
    }

    @Override
    @NotNull
    public SquareSnap getSnap() {
        return new SquareSnap(this);
    }

    public static final class SquareSnap implements Snap<Square> {

        @Nullable
        private final Id<AccountDao> occupant;

        public SquareSnap(@NotNull Square square) {
            this.occupant = square.occupant;
        }

        @Nullable
        public Id<AccountDao> getOccupant() {
            return occupant;
        }
    }

    @Override
    public String toString() {
        return "Square{" +
                "occupant=" + occupant +
                '}';
    }
}
