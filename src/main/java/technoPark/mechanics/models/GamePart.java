package technoPark.mechanics.models;

public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

    Snap<? extends GamePart> takeSnap();
}
