package technopark.mechanics.models;

import technopark.mechanics.Config;

public class Tiles {
    private final Coords centerPosition;
    private boolean isAlived;
    private boolean isBonus;
    private Config.Bonus bonus;
    private int indexPositionBonus;

    public Tiles(Coords centerPosition) {
        this.centerPosition = centerPosition;
        this.isAlived = true;
        this.isBonus = false;
        this.bonus = Config.Bonus.NOTHING;
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

    public boolean isBonus() {
        return isBonus;
    }

    public void setIsBonus(boolean bonus) {
        isBonus = bonus;
    }

    public Config.Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Config.Bonus bonus) {
        this.bonus = bonus;
    }

    public int getIndexPositionBonus() {
        return indexPositionBonus;
    }

    public void setIndexPositionBonus(int indexPositionBonus) {
        this.indexPositionBonus = indexPositionBonus;
    }
}
