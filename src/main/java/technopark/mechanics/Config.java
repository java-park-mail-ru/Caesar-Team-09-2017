package technopark.mechanics;

import technopark.mechanics.models.Coords;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("FieldNamingConvention")
public class Config {

    public static final int PLAYERS_SPEED = 4;
    public static final int FREE_FALL = 2;
    public static final int COIN_COST = 15;

    public static final long DRILING_COOLDOWN = 150;
    public static final long MOVEMENT_COOLDOWN = 50;
    public static final long JUMPING_COOLDOWN = 2000;
    public static final long START_SWITCH_DELAY = 5000;
    public static final long SWITCH_DELTA = 300;
    public static final long SWITCH_DELAY_MIN = 1500;

    public static final String SELF_COLOR = "#00cc00";
    public static final String ENEMY_COLOR = "#b36262";

    @SuppressWarnings("FieldNamingConvention")
    public enum Bonus {
        COIN,
        ENERGY_OF_BATTERY,
        NOTHING
    }

    @SuppressWarnings("FieldNamingConvention")
    public enum KeyDown {
        RIGHT,
        LEFT,
        NOTHING
    }

    public static final int WORLD_WIDTH = 1600;
    public static final int WORLD_HEIGHT = 2000;
    public static final int PLAYER_WIDTH = 32;
    public static final int PLAYER_HEIGHT = 32;
    public static final int COUNT_OF_BONUSES = 100;
    public static final int COIN_WIDTH = 32;
    public static final int COIN_HEIGHT = 32;
    public static final int GROUND_WIDTH = 32;
    public static final int GROUND_HEIGHT = 32;
    public static final int POSITION_GROUND = 6 * GROUND_HEIGHT;
    public static final int[] PLAYER_X = {GROUND_WIDTH * 8, WORLD_WIDTH - GROUND_WIDTH * 8};
    public static final int PLAYER_Y = POSITION_GROUND - PLAYER_HEIGHT;
    public static final int START_MONEY = 100;
    public static final int START_ENERGY = 30;
    public static Coords[] BONUS_POSITION;
    public static final int RADIUS_RADAR = 600;
    public static final int MAX_RADIUS_RADAR = 1600;
    public static final int COST_UPGRADE_ENERGY = 50;
    public static final int COST_UPGRADE_DRILL = 20;
    public static final int COST_UPGRADE_RADAR = 10;

    static {
        changeBonusPosition();
    }

    static void normalizedBonusPosition(Coords[] bonusPosition) {
        Arrays.sort(bonusPosition,
                Comparator.comparing(Coords::getY)
                        .thenComparing(Coords::getX));
    }

    public static void changeBonusPosition() {
        Coords[] bonusPosition = new Coords[COUNT_OF_BONUSES];
        int minX = 0;
        int maxX = WORLD_WIDTH / GROUND_WIDTH - 1;
        int minY = POSITION_GROUND;
        int maxY = (WORLD_HEIGHT - POSITION_GROUND) / GROUND_HEIGHT - 1;
        for (int i = 0; i < COUNT_OF_BONUSES; i++) {
            int ratioX = minX + (int) (Math.random() * maxX) * GROUND_WIDTH;
            int ratioY = minY + (int) (Math.random() * maxY) * GROUND_HEIGHT;
            bonusPosition[i] = new Coords(ratioX, ratioY);
        }
        normalizedBonusPosition(bonusPosition);
        BONUS_POSITION = bonusPosition;
    }
}
