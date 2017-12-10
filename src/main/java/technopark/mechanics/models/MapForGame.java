package technopark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.Config;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.part.PositionPart;
import technopark.mechanics.models.player.GameObject;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.models.id.Id;
import technopark.account.dao.AccountDao;

import java.util.ArrayList;
import java.util.List;

import static technopark.mechanics.Config.*;

public class MapForGame extends GameObject {

    @NotNull
    private final List<Id<AccountDao>> gameUserIds;

    @NotNull
    private final List<Boolean> isJump;

    @NotNull
    private final List<Integer> jumpFrameCount;

    @NotNull
    private final Tiles[] tiles; // по слоям

    @NotNull
    private final GameSession gameSession;

    private ArrayList<Coords> destroyedTiles;
    private ArrayList<Coords> destroyedBonus;

    private final int lengthX;
    private final int lengthY;

    private final int startPlayerY;

    public MapForGame(@NotNull GameSession gameSession) {
        this.gameSession = gameSession;
        gameUserIds = new ArrayList<>();
        isJump = new ArrayList<>();
        jumpFrameCount = new ArrayList<>();
        destroyedTiles = new ArrayList<>();
        destroyedBonus = new ArrayList<>();
        isJump.add(false);
        jumpFrameCount.add(0);
        gameUserIds.add((gameSession.getFirst().getAccountId()));
        gameSession.getUser(0).claimPart(PositionPart.class).setPosition(new Coords(PLAYER_X[0], PLAYER_Y));
        startPlayerY = PLAYER_Y;
        if (!gameSession.isSinglePlay()) {
            gameUserIds.add((gameSession.getSecond().getAccountId()));
            gameSession.getUser(1).claimPart(PositionPart.class).setPosition(new Coords(PLAYER_X[1], PLAYER_Y));
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
        final int indexOfUser = gameUserIds.indexOf(user);
        Coords userPosition =  gameSession.getUser(indexOfUser).claimPart(PositionPart.class).getPosition();
        final int i = findTile(coords);
        if (i != -1) {
            if (tiles[i].isAlived() && checkDrillForPosition(userPosition, tiles[i].getCenterPosition())) {
                tiles[i].setAlived(false);
                gameSession.getFirst().claimPart(MechanicPart.class).decrementEnergy();
                destroyedTiles.add(tiles[i].getCenterPosition());
            }
        }
    }

    private int findTile(@NotNull Coords coords) {
        int index;
        final int x = coords.x;
        final int y = coords.y;
        // сначала находим x
        for (index = 0; index < lengthX - 1; index++) {
            if (x >= tiles[index].getCenterPosition().x && x < tiles[index + 1].getCenterPosition().x) {
               break;
            }
        }
        // теперь y
        if (y == tiles[0].getCenterPosition().y - PLAYER_HEIGHT / 2) {
            return index;
        }

        for (int i = 0; i < lengthY - 1; i++) {
            if (y >= tiles[i * lengthX].getCenterPosition().y && y < tiles[(i + 1) * lengthX].getCenterPosition().y) {
                return index + lengthX * i;
            }
        }
        return -1;
    }

    private boolean checkDrillForPosition(Coords position, Coords checkedTile) {
        if (Math.abs(checkedTile.y - position.y) <= 2 * GROUND_HEIGHT && Math.abs(checkedTile.x - position.x) <= 2 * GROUND_WIDTH) {
            return true;
        }
        return false;
    }

    public void moveTo(@NotNull Move move, @NotNull Id<AccountDao> user) {
        final int indexOfUser = gameUserIds.indexOf(user);
        Coords userPosition =  gameSession.getUser(indexOfUser).claimPart(PositionPart.class).getPosition();
        Coords newUserPosition = null;
        switch (move.getKeyDown()) {
            case LEFT:
                if (userPosition.x - PLAYERS_SPEED >= 0) { // не выходит ли за пределы карты
                    newUserPosition = new Coords(userPosition.x - PLAYERS_SPEED, userPosition.y);
                    if (!checkMove(newUserPosition)
                            || !checkMove(new Coords(userPosition.x, userPosition.y + PLAYER_HEIGHT - FREE_FALL))) {
                        newUserPosition = null;
                    }

                } else {
                    newUserPosition = new Coords(0, userPosition.y);
                }
                break;
            case RIGHT:
                if ((userPosition.x + PLAYERS_SPEED) <= (WORLD_WIDTH - PLAYER_WIDTH)) {
                    newUserPosition = new Coords(userPosition.x + PLAYERS_SPEED, userPosition.y);
                    if (!checkMove(new Coords(userPosition.x + PLAYER_WIDTH, userPosition.y))
                            || !checkMove(new Coords(userPosition.x + PLAYER_WIDTH, userPosition.y + PLAYER_HEIGHT - FREE_FALL))) {
                        newUserPosition = null;
                    }

                } else {
                    newUserPosition = new Coords(WORLD_WIDTH - PLAYER_WIDTH, userPosition.y);
                }
                break;
            case NOTHING:
                break;
            default:
                break;
        }

        if (newUserPosition != null) {
            gameSession.getUser(indexOfUser).claimPart(PositionPart.class).setPosition(newUserPosition);
        }
    }

    private boolean checkMove(Coords newPosition) {
        int index = findTile(newPosition);
        if (newPosition.y > startPlayerY && index != -1 && tiles[index].isAlived()) {
            return false;
        }
        return true;
    }

    public void checkGravity(@NotNull Id<AccountDao> user) {
        final int indexOfUser = gameUserIds.indexOf(user);
        Coords userPosition =  gameSession.getUser(indexOfUser).claimPart(PositionPart.class).getPosition();
        Coords newUserPosition = null;
        final int i = findTile(new Coords(userPosition.x, userPosition.y + PLAYER_HEIGHT));
        boolean condition = checkMove(new Coords(userPosition.x, userPosition.y + PLAYER_HEIGHT));
        if (condition) {
            if (checkMove(new Coords(userPosition.x - PLAYER_WIDTH / 2, userPosition.y + PLAYER_HEIGHT))
                    && !checkMove(new Coords(userPosition.x + PLAYER_WIDTH / 2, userPosition.y + PLAYER_HEIGHT))) {
                condition = false;
            }
        }
        if ((userPosition.y != startPlayerY && i == -1) || condition) {
            if ((userPosition.y + FREE_FALL) <= (WORLD_HEIGHT - PLAYER_HEIGHT)) {
                newUserPosition = new Coords(userPosition.x, userPosition.y + FREE_FALL);
            } else {
                newUserPosition = new Coords(userPosition.x, WORLD_HEIGHT - PLAYER_HEIGHT);
            }
        }

        if (newUserPosition != null) {
            gameSession.getUser(indexOfUser).claimPart(PositionPart.class).setPosition(newUserPosition);
        }
    }

    public void startJump(@NotNull Id<AccountDao> user) {
        final int indexOfUser = gameUserIds.indexOf(user);
        if (!isJump.get(indexOfUser)) {
            isJump.set(indexOfUser, true);
            jumpFrameCount.set(indexOfUser, 12);
        }
    }

    private Coords jumpTo(int indexOfUser, int stage, Coords newUserPosition) {
        boolean condition = checkMove(newUserPosition);
        if (!condition) {
            newUserPosition = null;
            isJump.set(indexOfUser, false);
            jumpFrameCount.set(indexOfUser, 0);
        } else {
            jumpFrameCount.set(indexOfUser, --stage);
        }
        return newUserPosition;
    }

    public void checkJump(@NotNull Id<AccountDao> user) {
        final int indexOfUser = gameUserIds.indexOf(user);
        Coords userPosition =  gameSession.getUser(indexOfUser).claimPart(PositionPart.class).getPosition();
        Coords newUserPosition = null;

        if (isJump.get(indexOfUser)) {
            int stage = jumpFrameCount.get(indexOfUser);
            if (stage >= 5) {
                newUserPosition = new Coords(userPosition.x, userPosition.y - FREE_FALL * 2);
                newUserPosition = this.jumpTo(indexOfUser, stage, newUserPosition);
            } else if (stage >= 3) {
                newUserPosition = new Coords(userPosition.x, userPosition.y - FREE_FALL * 2 - 1);
                newUserPosition = this.jumpTo(indexOfUser, stage, newUserPosition);

            } else if (stage >= 1) {
                newUserPosition = new Coords(userPosition.x, userPosition.y - FREE_FALL * 2 - 2);
                newUserPosition = this.jumpTo(indexOfUser, stage, newUserPosition);

            } else {
                newUserPosition = new Coords(userPosition.x, userPosition.y - FREE_FALL * 2 - FREE_FALL);
                newUserPosition = this.jumpTo(indexOfUser, stage, newUserPosition);
                isJump.set(indexOfUser, false);
            }
        }

        if (newUserPosition != null) {
            gameSession.getUser(indexOfUser).claimPart(PositionPart.class).setPosition(newUserPosition);
        }
    }

    public void checkBonus(@NotNull Id<AccountDao> user) {
        final int indexOfUser = gameUserIds.indexOf(user);
        Coords userPosition =  gameSession.getUser(indexOfUser).claimPart(PositionPart.class).getPosition();
        final int i = findTile(userPosition);
        if (i != -1 && tiles[i].isBonus()) {
            destroyedBonus.add(BONUS_POSITION[tiles[i].getIndexPositionBonus()]);
            Config.Bonus bonus = tiles[i].getBonus();
            tiles[i].setIsBonus(false);
            switch (bonus) {
                case COIN:
                    gameSession.getUser(indexOfUser).claimPart(MechanicPart.class).changeMoney(COIN_COST);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    @NotNull
    public MapForGame.MapSnap getSnap() {
        return new MapSnap(this);
    }

    @SuppressWarnings("unused")
    public static final class MapSnap implements Snap<MapForGame> {

        private Coords[] destroyedTiles;

        private Coords[] destroyedBonus;

        @NotNull
        public MapSnap(@NotNull MapForGame mapForGame) {
            if (mapForGame.destroyedTiles != null) {
                this.destroyedTiles = mapForGame.destroyedTiles.toArray(new Coords[mapForGame.destroyedTiles.size()]);
                mapForGame.destroyedTiles.clear();
            } else {
                this.destroyedTiles = null;
            }

            if (mapForGame.destroyedBonus != null) {
                this.destroyedBonus = mapForGame.destroyedBonus.toArray(new Coords[mapForGame.destroyedBonus.size()]);
                mapForGame.destroyedBonus.clear();
            } else {
                this.destroyedBonus = null;
            }
        }

        public Coords[] getDestroyedTiles() {
            return destroyedTiles;
        }

        public Coords[] getDestroyedBonus() {
            return destroyedBonus;
        }
    }


}
