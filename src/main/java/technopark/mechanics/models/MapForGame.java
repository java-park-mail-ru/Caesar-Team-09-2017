package technopark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.models.part.GamePart;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.player.GameObject;
import technopark.mechanics.models.player.GameUserId;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.models.id.Id;
import technopark.model.account.dao.AccountDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static technopark.mechanics.Config.*;

public class MapForGame extends GameObject {

    @NotNull
    private final List<GameUserId> gameUserIds;

    @NotNull
    private final List<Coords> userPosition;

    @NotNull
    private final Tiles[] tilesPosition; // по слоям

    @NotNull
    private final GameSession gameSession;

    private Coords[] destroyedTiles;


    private final int lengthX;
    private final int lengthY;

    public MapForGame(@NotNull GameSession gameSession) {
        this.gameSession = gameSession;
        gameUserIds = new ArrayList<>();
        userPosition = new ArrayList<>();
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            gameUserIds.add(new GameUserId());
        }
        gameUserIds.get(0).setGameUserId(gameSession.getFirst().getAccountId());
        userPosition.add(new Coords(PLAYER_X, PLAYER_Y));
        if (!gameSession.isSinglePlay()) {
            gameUserIds.get(1).setGameUserId(gameSession.getSecond().getAccountId());
            userPosition.add(new Coords(PLAYER_X, PLAYER_Y));
        }
        lengthX = WORLD_WIDTH / GROUND_WIDTH;
        lengthY = (WORLD_HEIGHT - POSITION_GROUND) / GROUND_HEIGHT;
        tilesPosition = new Tiles[lengthX * lengthY]; // x * y
        for (int i = 0; i < lengthX * lengthY; i++) {
            int x = i * GROUND_WIDTH + GROUND_WIDTH / 2;
            int y = i * GROUND_HEIGHT + GROUND_HEIGHT / 2 + POSITION_GROUND;
            tilesPosition[i] = new Tiles(new Coords(x, y));
        }
    }

    public void drillAt(@NotNull Coords coords, @NotNull Id<AccountDao> user) {
        final int i = findTile(coords);
        if (i != -1) {
            if (tilesPosition[i].isAlived()) {
                tilesPosition[i].setAlived(false);
                gameSession.getFirst().claimPart(MechanicPart.class).decrementEnergy();
                destroyedTiles[0] = tilesPosition[i].getCenterPosition();
            }
        }
//        final Id<AccountDao> occupant = gameUserIds.get(i).getGameUserId();
//        if (occupant != null) {
//            gameSession.getEnemy(occupant).claimPart(MechanicPart.class).incrementScore();
//        }
//        gameSession.getFirst().claimPart(MechanicPart.class).decrementEnergy();
    }

    private int findTile(@NotNull Coords coords) {
        int i;
        final int x = coords.x;
        final int y = coords.y;
        for(i = 0; i < lengthY - 1; i++) {
            boolean conditionY = y >= tilesPosition[i].getCenterPosition().y && y < tilesPosition[i+1].getCenterPosition().y;
            boolean conditionX = x >= tilesPosition[i].getCenterPosition().x && x < tilesPosition[i+1].getCenterPosition().x;
            if (conditionY && conditionX) {
               return i;
            }
        }
        return -1;
    }

    public void moveTo(@NotNull Move move, @NotNull Id<AccountDao> user) {
        int i = userPosition.indexOf(user);
        System.out.println(i);
        switch (move.getKeyDown()) {
            case DOWN:
                if ((userPosition.get(0).y + PLAYERS_SPEED) <= (WORLD_HEIGHT - PLAYER_HEIGHT)) {
                    userPosition.get(0).y += PLAYERS_SPEED;
                } else {
                    userPosition.get(0).y = WORLD_HEIGHT - PLAYER_HEIGHT;
                }
                break;
            case UP:
                break;
            case LEFT:
                if ((userPosition.get(0).x - PLAYERS_SPEED) >= (0 + PLAYER_WIDTH)) {
                    userPosition.get(0).x -= PLAYERS_SPEED;
                } else {
                    userPosition.get(0).x = PLAYER_WIDTH;
                }
                break;
            case RIGHT:
                if ((userPosition.get(0).x + PLAYERS_SPEED) <= (WORLD_WIDTH - PLAYER_WIDTH)) {
                    userPosition.get(0).x += PLAYERS_SPEED;
                } else {
                    userPosition.get(0).x = WORLD_WIDTH - PLAYER_WIDTH;
                }
                break;
            case SPACE:
//                TODO: сделать прыжок
                break;
            case NOTHING:
                break;
            default:
                break;
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

        @NotNull
        public MapSnap(@NotNull MapForGame mapForGame) {
            this.destroyedTiles = mapForGame.destroyedTiles;
            this.userPosition = mapForGame.userPosition;
        }

        public Coords[] getDestroyedTiles() {
            return destroyedTiles;
        }

        public List<Coords> getUserPosition() {
            return userPosition;
        }
    }


}
