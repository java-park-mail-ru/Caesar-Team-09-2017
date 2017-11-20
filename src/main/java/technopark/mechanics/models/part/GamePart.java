package technopark.mechanics.models.part;

import technopark.mechanics.models.Snap;

public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

    Snap<? extends GamePart> takeSnap();
}
