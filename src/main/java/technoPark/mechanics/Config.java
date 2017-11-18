package technoPark.mechanics;

public class Config {
    public static final int SQUARE_SIZE = 250;

    public static final int SQUARES_COUNT = 9;
    public static final int SQUARES_IN_A_ROW = 3;

    public static final long DRILING_COOLDOWN = 150;
    public static final long START_SWITCH_DELAY = 5000;
    public static final long SWITCH_DELTA = 300;
    public static final long SWITCH_DELAY_MIN = 1500;

    public static final String SELF_COLOR = "#00cc00";
    public static final String ENEMY_COLOR = "#b36262";
    public static final int SCORES_TO_WIN = 100;

    @SuppressWarnings("FieldNamingConvention")
    public enum Bonus {
        NAME_OF_BONUS_1,
        NAME_OF_BONUS_2,
        NAME_OF_BONUS_3
    }

    @SuppressWarnings("FieldNamingConvention")
    public enum KeyDown {
        UP,
        DOWN, // Serega
        RIGHT,
        LEFT,
        SPACE
    }

    public static final int WORLD_WIDTH = 1600;
    public static final int WORLD_HEIGHT = 2000;
    public static final int PLAYER_X = WORLD_WIDTH / 2;
    public static final int PLAYER_Y = WORLD_HEIGHT / 2;
    public static final int PLAYER_WIDTH = 32;
    public static final int PLAYER_HEIGHT = 32;
    public static final int COINS = 10;
    public static final int COIN_WIDTH = 64;
    public static final int COINT_HEIGHT = 64;
    public static final int GROUND_WIDTH = 16;
    public static final int GROUND_HEIGHT = 16;
    public static final int[][] MAP = {{0, 0, 0, 1}, {1, 1, 1, 1}, {1, 2, 1, 1}, {1, 1, 1, 2}};

}
