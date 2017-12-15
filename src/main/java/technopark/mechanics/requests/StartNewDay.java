package technopark.mechanics.requests;

import org.jetbrains.annotations.NotNull;
import technopark.websocket.MessageRequest;

public class StartNewDay extends MessageRequest {
    @NotNull
    private int drillDiff;
    @NotNull
    private int energyDiff;
    @NotNull
    private int radiusRadarDiff;

    public int getDrillDiff() {
        return drillDiff;
    }

    public void setDrillDiff(int drillDiff) {
        this.drillDiff = drillDiff;
    }

    public int getEnergyDiff() {
        return energyDiff;
    }

    public void setEnergyDiff(int energyDiff) {
        this.energyDiff = energyDiff;
    }

    public int getRadiusRadarDiff() {
        return radiusRadarDiff;
    }

    public void setRadiusRadarDiff(int radiusRadarDiff) {
        this.radiusRadarDiff = radiusRadarDiff;
    }
}
