package technopark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.Config;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.player.GameObject;
import technopark.mechanics.models.player.GameUserId;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.models.id.Id;
import technopark.model.account.dao.AccountDao;

import java.util.ArrayList;
import java.util.List;

import static technopark.mechanics.Config.*;

public class MapForGame extends GameObject {

    @NotNull
    private final List<Id<AccountDao>> gameUserIds;

    @NotNull
    private final List<Coords> userPosition;

    @NotNull
    private final List<Boolean> isJump;

    @NotNull
    private final List<Integer> jumpFrameCount;

    @NotNull
    private final Tiles[] tiles; // по слоям

    @NotNull
    private final GameSession gameSession;

    private Coords[] destroyedTiles;
    private Coords destroyedBonus;

    private final int lengthX;
    private final int lengthY;

    public MapForGame(@NotNull GameSession gameSession) {
        this.gameSession = gameSession;
        gameUserIds = new ArrayList<>();
        userPosition = new ArrayList<>();
        isJump = new ArrayList<>();
        jumpFrameCount = new ArrayList<>();
        destroyedTiles = new Coords[1];

        isJump.add(false);
        jumpFrameCount.add(0);
        gameUserIds.add((gameSession.getFirst().getAccountId()));
        userPosition.add(new Coords(PLAYER_X, PLAYER_Y));

        if (!gameSession.isSinglePlay()) {
            gameUserIds.add((gameSession.getSecond().getAccountId()));
            userPosition.add(new Coords(PLAYER_X, PLAYER_Y));
            isJump.add(false);
            jumpFrameCount.add(0);
        }

        lengthX = WORLD_WIDTH / GROUND_WIDTH;
        lengthY = (WORLD_HEIGHT - POSITION_GROUND) / GROUND_HEIGHT;
        tiles = new Tiles[lengthX * lengthY]; // x * y
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < lengthY; i++) {
            for (int j = 0; j < lengthX; j++) {
                int x = j * GROUND_WIDTH;
                int y = i * GROUND_HEIGHT + POSITION_GROUND;
                tiles[i * lengthX + j] = new Tiles(new Coords(x, y));
            }
        }

        for (int i = 0; i < COUNT_OF_BONUSES; i++) {
            int index = findTile(BONUS_POSITION[i]);
            if (index == -1) {
                System.out.println("BONUS problems");
                System.out.println(BONUS_POSITION[i]);
            } else {
                tiles[index].setIsBonus(true);
                tiles[index].setBonus(Config.Bonus.COIN);
                tiles[index].setIndexPositionBonus(i);
            }
        }
    }

    public void drillAt(@NotNull Coords coords, @NotNull Id<AccountDao> user) {
        final int i = findTile(coords);
        if (i != -1) {
            System.out.println("i != -1");
            if (tiles[i].isAlived() && checkDrillForPosition(userPosition.get(0), tiles[i].getCenterPosition())) {
                System.out.println("checkDrillForPosition");
                tiles[i].setAlived(false);
                gameSession.getFirst().claimPart(MechanicPart.class).decrementEnergy();
                destroyedTiles[0] = tiles[i].getCenterPosition();
            }
        }
    }

    private int findTile(@NotNull Coords coords) {
        int index;
        final int x = coords.x + GROUND_WIDTH / 2;
        final int y = coords.y + GROUND_HEIGHT / 2;
        // сначала находим x
        for (index = 0; index < lengthX - 1; index++) {
            boolean conditionX = x >= tiles[index].getCenterPosition().x && x < tiles[index + 1].getCenterPosition().x;
            if (conditionX) {
               break;
            }
        }
        // теперь y
        for (int i = 0; i < lengthY - 1; i++) {
            boolean conditionY = y >= tiles[i].getCenterPosition().y && y < tiles[i * lengthX + 1].getCenterPosition().y;
            if (conditionY) {
                return index + lengthX * i;
            }
        }
        return -1;
    }

    private boolean checkDrillForPosition(Coords position, Coords checkedTile) {
        System.out.println("isAlived");
        if (Math.abs(checkedTile.y - position.y) <= 3 * GROUND_HEIGHT && Math.abs(checkedTile.x - position.x) <= 3 * GROUND_WIDTH) {
            return true;
        }
        return false;
    }

    public void moveTo(@NotNull Move move, @NotNull Id<AccountDao> user) {
        // int i = userPosition.indexOf(user);
        switch (move.getKeyDown()) {
            case DOWN:
                break;
            case UP:
                isJump.add(0, true);
                jumpFrameCount.add(0, 6);
                break;
            case LEFT:
                if ((userPosition.get(0).x - PLAYERS_SPEED) >= (0 + PLAYER_WIDTH)) { // не выходит ли за пределы карты
                    userPosition.get(0).x -= PLAYERS_SPEED;
                    if (!checkMove(userPosition.get(0))) { // не собирается ли двинуться в место где есть тайл
                        userPosition.get(0).x += PLAYERS_SPEED;
                    }
                } else {
                    userPosition.get(0).x = PLAYER_WIDTH;
                }
                break;
            case RIGHT:
                if ((userPosition.get(0).x + PLAYERS_SPEED) <= (WORLD_WIDTH - PLAYER_WIDTH)) {
                    userPosition.get(0).x += PLAYERS_SPEED;
                    if (!checkMove(userPosition.get(0))) {
                        userPosition.get(0).x -= PLAYERS_SPEED;
                    }
                } else {
                    userPosition.get(0).x = WORLD_WIDTH - PLAYER_WIDTH;
                }
                break;
            case SPACE:
                // TODO : сделать игровое действие
                break;
            case NOTHING:
                break;
            default:
                break;
        }
        checkGravity(user);
        checkJump(user);
        checkBonus(user);
    }

    private boolean checkMove(Coords newPosition) {
        int index = findTile(newPosition);
        if (index != -1 && tiles[index].isAlived()) {
            return false;
        }
        return true;
    }

    private void checkGravity(@NotNull Id<AccountDao> user) {
        Coords tileUnderPlayer = new Coords(userPosition.get(0).x, userPosition.get(0).y + GROUND_HEIGHT);
        final int i = findTile(tileUnderPlayer);
        if (i != -1 && !tiles[i].isAlived()) {
            if ((userPosition.get(0).y + FREE_FALL) <= (WORLD_HEIGHT - PLAYER_HEIGHT)) {
                userPosition.get(0).y += FREE_FALL;
            } else {
                userPosition.get(0).y = WORLD_HEIGHT - PLAYER_HEIGHT;
            }
        }
    }

    private void checkJump(@NotNull Id<AccountDao> user) {
        if (isJump.get(0) == Boolean.TRUE) {
            switch (jumpFrameCount.get(0)) {
                case 6:
                case 5:
                    userPosition.get(0).y -= FREE_FALL * 2;
                    break;
                case 4:
                case 3:
                    userPosition.get(0).y -= FREE_FALL * 2 - 1;
                    break;
                case 2:
                case 1:
                    userPosition.get(0).y -= FREE_FALL * 2 - 2;
                    break;
                case 0:
                    userPosition.get(0).y -= FREE_FALL;
                    isJump.add(0, false);
            }
        }
    }

    private void checkBonus(@NotNull Id<AccountDao> user) {
        final int i = findTile(userPosition.get(0));
        if (i != -1 && tiles[i].isBonus()) {
            destroyedBonus = BONUS_POSITION[tiles[i].getIndexPositionBonus()];
            Config.Bonus bonus = tiles[i].getBonus();
            tiles[i].setIsBonus(false);
            switch (bonus) {
                case COIN:
                    gameSession.getFirst().claimPart(MechanicPart.class).changeMoney(COIN_COST);
                    break;
                default:
                    break;
            }
        }
    }

    public List<Coords> getUserPosition() {
        return userPosition;
    }

    @Override
    @NotNull
    public MapForGame.MapSnap getSnap() {
        return new MapSnap(this);
    }

    @SuppressWarnings("unused")
    public static final class MapSnap implements Snap<MapForGame> {

        private Coords[] destroyedTiles;

        @NotNull
        private final List<Coords> userPosition;

        private Coords destroyedBonus;

        @NotNull
        public MapSnap(@NotNull MapForGame mapForGame) {
            this.destroyedTiles = mapForGame.destroyedTiles;
            mapForGame.destroyedTiles = null;
            this.userPosition = mapForGame.userPosition;
            this.destroyedBonus = mapForGame.destroyedBonus;
        }

        public Coords[] getDestroyedTiles() {
            return destroyedTiles;
        }

        public List<Coords> getUserPosition() {
            return userPosition;
        }

        public Coords getDestroyedBonus() {
            return destroyedBonus;
        }
    }


}
