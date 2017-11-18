package technoPark.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.Config;
import technoPark.mechanics.models.Coords;
import technoPark.websocket.MessageRequest;

@SuppressWarnings({"NullableProblems"})
public class ClientSnap extends MessageRequest {


    private Config.KeyDown keyDown;

    @NotNull
    private Coords mouse;

    private boolean isDrill;
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
}

/*
{
"isDrill": bool,
"mouse": {
    x: float,
    y: float
    },
"frameTime": long,
"keyDown": enum
}
 */