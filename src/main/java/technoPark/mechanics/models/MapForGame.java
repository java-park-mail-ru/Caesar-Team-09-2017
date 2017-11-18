package technoPark.mechanics.models;

import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.models.session.GameSession;
import technoPark.mechanics.models.part.GamePart;
import technoPark.mechanics.models.player.GameObject;
import technoPark.mechanics.models.player.GameUserId;
import technoPark.model.id.Id;
import static technoPark.mechanics.Config.PLAYERS_COUNT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapForGame extends GameObject {

    @NotNull
    private final List<GameUserId> gameUserIds;

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
    }

    public void drillAt(@NotNull Coords coords) {
//        final int i = (int) (coords.x / Config.SQUARE_SIZE) + ((int)(coords.y / Config.SQUARE_SIZE)) * Config.SQUARES_IN_A_ROW;
//        if (i < 0 || i > 8) {
//            return;
//        }
//
//        final Id<AccountDao> occupant = gameUserIds.get(i).getGameUserId();
//        if (occupant != null) {
//            gameSession.getEnemy(occupant).claimPart(MechanicPart.class).incrementScore();
//        }
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
