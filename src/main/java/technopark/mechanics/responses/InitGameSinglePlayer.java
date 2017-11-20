package technopark.mechanics.responses;

import technopark.websocket.MessageResponse;

public class InitGameSinglePlayer {
    public static final class Response extends MessageResponse {
        private int worldWidth;
        private int worldHeight;
        private int playerX;
        private int playerY;
        private int playerWidth;
        private int playerHeight;
        private int countOfBonuses;
        private int coinWidth;
        private int coinHeight;
        private int groundWidth;
        private int groundHeight;
        private int[][] map;
        private int startMoney;
        private int startEnergy;

        public int getStartEnergy() {
            return startEnergy;
        }

        public void setStartEnergy(int startEnergy) {
            this.startEnergy = startEnergy;
        }

        public int getStartMoney() {
            return startMoney;
        }

        public void setStartMoney(int startMoney) {
            this.startMoney = startMoney;
        }

        public int getWorldWidth() {
            return worldWidth;
        }

        public void setWorldWidth(int worldWidth) {
            this.worldWidth = worldWidth;
        }

        public int getWorldHeight() {
            return worldHeight;
        }

        public void setWorldHeight(int worldHeight) {
            this.worldHeight = worldHeight;
        }

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

        public int getPlayerWidth() {
            return playerWidth;
        }

        public void setPlayerWidth(int playerWidth) {
            this.playerWidth = playerWidth;
        }

        public int getPlayerHeight() {
            return playerHeight;
        }

        public void setPlayerHeight(int playerHeight) {
            this.playerHeight = playerHeight;
        }

        public int getCountOfBonuses() {
            return countOfBonuses;
        }

        public void setCountOfBonuses(int countOfBonuses) {
            this.countOfBonuses = countOfBonuses;
        }

        public int getCoinWidth() {
            return coinWidth;
        }

        public void setCoinWidth(int coinWidth) {
            this.coinWidth = coinWidth;
        }

        public int getCoinHeight() {
            return coinHeight;
        }

        public void setCoinHeight(int coinHeight) {
            this.coinHeight = coinHeight;
        }

        public int getGroundWidth() {
            return groundWidth;
        }

        public void setGroundWidth(int groundWidth) {
            this.groundWidth = groundWidth;
        }

        public int getGroundHeight() {
            return groundHeight;
        }

        public void setGroundHeight(int groundHeight) {
            this.groundHeight = groundHeight;
        }

        public int[][] getMap() {
            return map;
        }

        public void setMap(int[][] map) {
            this.map = map;
        }
    }

}
