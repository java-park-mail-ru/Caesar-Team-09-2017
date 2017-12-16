package technopark.mechanics.requests;

import org.jetbrains.annotations.NotNull;
import technopark.websocket.MessageRequest;

public class Upgrade extends MessageRequest {
    @NotNull
    private boolean drill;
    @NotNull
    private boolean energy;
    @NotNull
    private boolean radiusRadar;

    public boolean isDrill() {
        return drill;
    }

    public void setDrill(boolean drill) {
        this.drill = drill;
    }

    public boolean isEnergy() {
        return energy;
    }

    public void setEnergy(boolean energy) {
        this.energy = energy;
    }

    public boolean isRadiusRadar() {
        return radiusRadar;
    }

    public void setRadiusRadar(boolean radiusRadar) {
        this.radiusRadar = radiusRadar;
    }
}
