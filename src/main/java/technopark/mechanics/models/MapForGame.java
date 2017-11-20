package technopark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.models.part.GamePart;
import technopark.mechanics.models.player.GameObject;
import technopark.mechanics.models.player.GameUserId;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.models.id.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static technopark.mechanics.Config.*;

public class MapForGame extends GameObject {

    @NotNull
    private final List<GameUserId> gameUserIds;

    //    TODO: для мультиплеер нужен список координат
    @NotNull
    private float playerX;
    private float playerY;

    @NotNull
    private final GameSession gameSession;

    public MapForGame(@NotNull GameSession gameSession) {
        this.gameSession = gameSession;
        gameUserIds = new ArrayList<>();
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            gameUserIds.add(new GameUserId());
        }
        gameUserIds.get(0).setGameUserId(gameSession.getFirst().getAccountId());
        if (!gameSession.isSinglePlay()) {
            gameUserIds.get(1).setGameUserId(gameSession.getSecond().getAccountId());
        }
        this.playerX = PLAYER_X;
        this.playerY = PLAYER_Y;
    }

    public void drillAt(@NotNull Coords coords) {
//        TODO: понять какой из игроков поля бурит
//        final Id<AccountDao> occupant = gameUserIds.get(i).getGameUserId();
//        if (occupant != null) {
//            gameSession.getEnemy(occupant).claimPart(MechanicPart.class).incrementScore();
//        }
//        gameSession.getFirst().claimPart(MechanicPart.class).decrementEnergy();
    }

    public void moveTo(@NotNull Move move) {
//        TODO: понять какой из игроков поля сделал движение
        switch (move.getKeyDown()) {
            case DOWN:
                playerY += PLAYERS_SPEED;
                break;
            case UP:
                break;
            case LEFT:
                playerX -= PLAYERS_SPEED;
                break;
            case RIGHT:
                playerX += PLAYERS_SPEED;
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

    @Override
    @NotNull
    public BoardSnap getSnap() {
        return new BoardSnap(this);
    }

    @SuppressWarnings("unused")
    public static final class BoardSnap implements Snap<MapForGame> {

        @NotNull
        private final List<Snap<? extends GamePart>> partSnaps;

        @NotNull
        private final List<Snap<GameUserId>> squares;

        @NotNull
        private final Id<GameObject> id;

        public BoardSnap(@NotNull MapForGame mapForGame) {
            this.partSnaps = mapForGame.getPartSnaps();
            this.id = mapForGame.getId();
            this.squares = mapForGame.gameUserIds.stream()
                    .map(GameUserId::getSnap)
                    .collect(Collectors.toList());
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
    }


}
