package technoPark.mechanics.models;

import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.multi.GameSession;
import technoPark.model.id.Id;
import static technoPark.mechanics.Config.SQUARES_COUNT;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Board extends GameObject {

    @NotNull
    private final List<Square> squares;

    @NotNull
    private final GameSession gameSession;

    public Board(@NotNull GameSession gameSession) {
        this.gameSession = gameSession;
        squares = new ArrayList<>();
        for (int i = 0; i < SQUARES_COUNT; i++) {
            squares.add(new Square());
        }
        squares.get(0).setOccupant(gameSession.getFirst().getAccountId());
        squares.get(1).setOccupant(gameSession.getSecond().getAccountId());
    }

    public void drillAt(@NotNull Coords coords) {
//        final int i = (int) (coords.x / Config.SQUARE_SIZE) + ((int)(coords.y / Config.SQUARE_SIZE)) * Config.SQUARES_IN_A_ROW;
//        if (i < 0 || i > 8) {
//            return;
//        }
//
//        final Id<AccountDao> occupant = squares.get(i).getOccupant();
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
    public static final class BoardSnap implements Snap<Board> {

        @NotNull
        private final List<Snap<? extends GamePart>> partSnaps;

        @NotNull
        private final List<Snap<Square>> squares;

        @NotNull
        private final Id<GameObject> id;

        public BoardSnap(@NotNull Board board) {
            this.partSnaps = board.getPartSnaps();
            this.id = board.getId();
            this.squares = board.squares.stream()
                    .map(Square::getSnap)
                    .collect(Collectors.toList());
        }

        @NotNull
        public Id<GameObject> getId() {
            return id;
        }

        @NotNull
        public List<Snap<Square>> getSquares() {
            return squares;
        }

        @NotNull
        public List<Snap<? extends GamePart>> getPartSnaps() {
            return partSnaps;
        }
    }


}
