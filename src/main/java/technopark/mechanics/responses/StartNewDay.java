package technopark.mechanics.responses;

import technopark.websocket.MessageResponse;

public class StartNewDay extends MessageResponse {
    public static final class Response extends MessageResponse {
        private int playerX;
        private int playerY;

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
    }
}
