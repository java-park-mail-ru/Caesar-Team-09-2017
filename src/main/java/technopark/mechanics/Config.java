package technopark.mechanics;

import technopark.mechanics.models.Coords;

import java.util.Arrays;

public class Config {

    public static final int PLAYERS_COUNT = 2;
    public static final int PLAYERS_SPEED = 3;
    public static final int FREE_FALL = 3;
    public static final int COIN_COST = 15;

    public static final long DRILING_COOLDOWN = 150;
    public static final long MOVEMENT_COOLDOWN = 50;
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
        UP,
        DOWN,
        RIGHT,
        LEFT,
        SPACE,
        NOTHING
    }

    public static final int WORLD_WIDTH = 1600;
    public static final int WORLD_HEIGHT = 2000;
    public static final int POSITION_GROUND = 200;
    public static final int PLAYER_WIDTH = 32;
    public static final int PLAYER_HEIGHT = 32;
    public static final int PLAYER_X = WORLD_WIDTH / 2;
    public static final int PLAYER_Y = POSITION_GROUND - PLAYER_HEIGHT / 2;
    public static final int COUNT_OF_BONUSES = 20;
    public static final int COIN_WIDTH = 32;
    public static final int COIN_HEIGHT = 32;
    public static final int GROUND_WIDTH = 32;
    public static final int GROUND_HEIGHT = 32;
    public static final int START_MONEY = 100;
    public static final int START_ENERGY = 100;
    public static final Coords[] BONUS_POSITION;

    static {
        Coords[] bonusPosition = new Coords[COUNT_OF_BONUSES];
        int minX = COIN_WIDTH / 2;
        int maxX = WORLD_WIDTH - COIN_WIDTH / 2 - minX;
        int minY = COIN_HEIGHT / 2 + POSITION_GROUND;
        int maxY = WORLD_HEIGHT - COIN_HEIGHT / 2 - minY;
        for (int i = 0; i < COUNT_OF_BONUSES; i++) {
            int ratioX = minX + (int) (Math.random() * maxX);
            int ratioY = minY + (int) (Math.random() * maxY);
            bonusPosition[i] = new Coords(ratioX, ratioY);
        }
        normalizedBonusPosition(bonusPosition);
        BONUS_POSITION = bonusPosition;
    }

    static void normalizedBonusPosition(Coords[] bonusPosition) {
        Arrays.sort(bonusPosition, (obj1, obj2) -> {
            if (obj1.y > obj2.y) {
                return 1;
            } else if (obj1.y == obj2.y) {

                if (obj1.x > obj2.x) {
                    return 1;
                } else if (obj1.x < obj2.x) {
                    return -1;
                }
                return 0;

            }
            return -1;
        });
    }
}
