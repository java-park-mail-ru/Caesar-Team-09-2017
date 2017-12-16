package technopark.mechanics.responses;

import technopark.websocket.MessageResponse;

public class Upgrade extends MessageResponse {
    public static final class Response extends MessageResponse {
        private int radiusRadar;
        private int energy;
        private boolean successfully;

        public boolean isSuccessfully() {
            return successfully;
        }

        public void setSuccessfully(boolean successfully) {
            this.successfully = successfully;
        }

        public int getRadiusRadar() {
            return radiusRadar;
        }

        public void setRadiusRadar(int radiusRadar) {
            this.radiusRadar = radiusRadar;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }
    }
}
