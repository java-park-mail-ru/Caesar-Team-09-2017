package technopark.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import technopark.mechanics.models.Bonus;
import technopark.mechanics.models.Coords;
import technopark.mechanics.models.Move;
import technopark.websocket.MessageRequest;

@SuppressWarnings({"NullableProblems"})
public class ClientSnap extends MessageRequest {

    @NotNull
    private Coords mouse;

    @NotNull
    private Move move;

    @NotNull
    private Bonus bonus;

    private boolean isDrill;
    private boolean isBonus;
    private long frameTime;

    @NotNull
    public Coords getMouse() {
        return mouse;
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

    public void setDrill(boolean drill) {
        isDrill = drill;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }

    public boolean isBonus() {
        return isBonus;
    }

    public void setBonus(boolean bonus) {
        isBonus = bonus;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}