package technoPark.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.Config;
import technoPark.mechanics.models.Coords;
import technoPark.websocket.MessageRequest;

@SuppressWarnings({"NullableProblems"})
public class ClientSnap extends MessageRequest {

    private Config.KeyDown keyDown;
    private Config.Bonus bonus;

    @NotNull
    private Coords mouse;

    private boolean isDrill;
    private boolean isBonus;
    private long frameTime;

    @NotNull
    public Coords getMouse() {
        return mouse;
    }

    public Config.KeyDown getKeyDown() {
        return keyDown;
    }

    @JsonProperty("isDrill")
    public boolean isDrill() {
        return isDrill;
    }

    public long getFrameTime() {
        return frameTime;
    }

    public void setMouse(@NotNull Coords mouse) {
        this.mouse = mouse;
    }

    public void setKeyDown(Config.KeyDown keyDown) {
        this.keyDown = keyDown;
    }

    public void setDrill(boolean drill) {
        isDrill = drill;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }

    public Config.Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Config.Bonus bonus) {
        this.bonus = bonus;
    }

    public boolean isBonus() {
        return isBonus;
    }

    public void setBonus(boolean bonus) {
        isBonus = bonus;
    }
}