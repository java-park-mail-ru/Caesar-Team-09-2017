package technopark.mechanics.responses;

import technopark.mechanics.models.Coords;
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
        private int startMoney;
        private int startEnergy;
        private int positionGround;
        private Coords[] bonusPosition;
        private long userId;
        private int radiusRadar;
        private int maxRadiusRadar;
        private int costUpgradeEnergy;
        private int costUpgradeDrill;
        private int costUpgradeRadar;

        public int getCostUpgradeEnergy() {
            return costUpgradeEnergy;
        }

        public void setCostUpgradeEnergy(int costUpgradeEnergy) {
            this.costUpgradeEnergy = costUpgradeEnergy;
        }

        public int getCostUpgradeDrill() {
            return costUpgradeDrill;
        }

        public void setCostUpgradeDrill(int costUpgradeDrill) {
            this.costUpgradeDrill = costUpgradeDrill;
        }

        public int getCostUpgradeRadar() {
            return costUpgradeRadar;
        }

        public void setCostUpgradeRadar(int costUpgradeRadar) {
            this.costUpgradeRadar = costUpgradeRadar;
        }

        public int getMaxRadiusRadar() {
            return maxRadiusRadar;
        }

        public void setMaxRadiusRadar(int maxRadiusRadar) {
            this.maxRadiusRadar = maxRadiusRadar;
        }

        public int getRadiusRadar() {
            return radiusRadar;
        }

        public void setRadiusRadar(int radiusRadar) {
            this.radiusRadar = radiusRadar;
        }

        public Coords[] getBonusPosition() {
            return bonusPosition;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setBonusPosition(Coords[] bonusPosition) {
            this.bonusPosition = bonusPosition;
        }

        public int getPositionGround() {
            return positionGround;
        }

        public void setPositionGround(int positionGround) {
            this.positionGround = positionGround;
        }

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

    }

}
