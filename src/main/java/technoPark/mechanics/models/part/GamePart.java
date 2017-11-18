package technoPark.mechanics.models.part;

import technoPark.mechanics.models.Snap;

public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

    Snap<? extends GamePart> takeSnap();
}
