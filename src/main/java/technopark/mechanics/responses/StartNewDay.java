package technopark.mechanics.responses;

import technopark.mechanics.models.Coords;
import technopark.websocket.MessageResponse;

public class StartNewDay extends MessageResponse {
    public static final class Response extends MessageResponse {
        private int playerX;
        private int playerY;
        private Coords[] bonusPosition;

        public int getPlayerX() {
            return playerX;
        }

        public void setPlayerX(int playerX) {
            this.playerX = playerX;
        }

        public int getPlayerY() {
            return playerY;
        }

        public void setPlayerY(int playerY) {
            this.playerY = playerY;
        }

        public Coords[] getBonusPosition() {
            return bonusPosition;
        }

        public void setBonusPosition(Coords[] bonusPosition) {
            this.bonusPosition = bonusPosition;
        }
    }
}
