package technopark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.models.part.GamePart;
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
    private final List<Coords> moveDifference;

//    @NotNull
//    private final Tiles[] tilesPosition; // по слоям

    @NotNull
    private final GameSession gameSession;

    public MapForGame(@NotNull GameSession gameSession) {
        this.gameSession = gameSession;
        gameUserIds = new ArrayList<>();
        userPosition = new ArrayList<>();
        moveDifference = new ArrayList<>();
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            gameUserIds.add(new GameUserId());
        }
        gameUserIds.get(0).setGameUserId(gameSession.getFirst().getAccountId());
        userPosition.add(new Coords(PLAYER_X, PLAYER_Y));
        moveDifference.add(new Coords(0, 0));
        if (!gameSession.isSinglePlay()) {
            gameUserIds.get(1).setGameUserId(gameSession.getSecond().getAccountId());
            userPosition.add(new Coords(PLAYER_X, PLAYER_Y));
            moveDifference.add(new Coords(0, 0));
        }
    }

    public void drillAt(@NotNull Coords coords, @NotNull Id<AccountDao> user) {
//        final Id<AccountDao> occupant = gameUserIds.get(i).getGameUserId();
//        if (occupant != null) {
//            gameSession.getEnemy(occupant).claimPart(MechanicPart.class).incrementScore();
//        }
//        gameSession.getFirst().claimPart(MechanicPart.class).decrementEnergy();
    }

    public void moveTo(@NotNull Move move, @NotNull Id<AccountDao> user) {
        int i = userPosition.indexOf(user);
        System.out.println(i);
        switch (move.getKeyDown()) {
            case DOWN:
                userPosition.get(0).y += PLAYERS_SPEED;
                moveDifference.get(0).y = PLAYERS_SPEED;
                break;
            case UP:
                break;
            case LEFT:
                userPosition.get(0).x -= PLAYERS_SPEED;
                moveDifference.get(0).x = -PLAYERS_SPEED;
                break;
            case RIGHT:
                userPosition.get(0).x += PLAYERS_SPEED;
                moveDifference.get(0).x = PLAYERS_SPEED;
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

    public List<Coords> getMoveDifference() {
        return moveDifference;
    }

    @Override
    @NotNull
    public MapForGame.MapSnap getSnap() {
        return new MapSnap(this);
    }

    @SuppressWarnings("unused")
    public static final class MapSnap implements Snap<MapForGame> {

        @NotNull
        private final List<Snap<? extends GamePart>> partSnaps;

        @NotNull
        private final List<Snap<GameUserId>> squares;

        @NotNull
        private final Id<GameObject> id;

        @NotNull
        private final List<Coords> moveDifference;

        public MapSnap(@NotNull MapForGame mapForGame) {
            this.partSnaps = mapForGame.getPartSnaps();
            this.id = mapForGame.getId();
            this.squares = mapForGame.gameUserIds.stream()
                    .map(GameUserId::getSnap)
                    .collect(Collectors.toList());
            this.moveDifference = mapForGame.getMoveDifference();
        }

        @NotNull
        public Id<GameObject> getId() {
            return id;
        }

        @NotNull
        public List<Snap<GameUserId>> getSquares() {
            return squares;
        }

        @NotNull
        public List<Snap<? extends GamePart>> getPartSnaps() {
            return partSnaps;
        }

        @NotNull
        public List<Coords> getMoveDifference() {
            return moveDifference;
        }
    }


}
