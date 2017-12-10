package technopark.mechanics.models.part;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.models.Coords;
import technopark.mechanics.models.Snap;

public class PositionPart implements GamePart {

    @NotNull
    private Coords position;

    public PositionPart() {
        this.position = new Coords(0, 0);
    }

    @NotNull
    public Coords getPosition() {
        return position;
    }

    public void setPosition(@NotNull Coords mouse) {
        this.position = mouse;
    }

    @Override
    public PositionSnap takeSnap() {
        return new PositionSnap(this);
    }

    public static final class PositionSnap implements Snap<MousePart> {

        private final Coords position;

        public PositionSnap(PositionPart positionPart) {
            this.position = positionPart.getPosition();
        }

        public Coords getPosition() {
            return position;
        }
    }
}

