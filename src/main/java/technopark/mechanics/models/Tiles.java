package technopark.mechanics.models;

public class Tiles {
    private final Coords centerPosition;
    private boolean isAlived;

    public Tiles(Coords centerPosition) {
        this.centerPosition = centerPosition;
        this.isAlived = true;
    }

    public Coords getCenterPosition() {
        return centerPosition;
    }

    public boolean isAlived() {
        return isAlived;
    }

    public void setAlived(boolean alived) {
        isAlived = alived;
    }
}
