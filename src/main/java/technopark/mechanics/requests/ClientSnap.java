package technopark.mechanics.requests;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.models.Bonus;
import technopark.mechanics.models.Coords;
import technopark.mechanics.models.Move;
import technopark.websocket.MessageRequest;

@SuppressWarnings({"NullableProblems"})
public class ClientSnap extends MessageRequest {

    private Coords mouse;

    private Move move;

    private boolean isDrill;
    private boolean isBonus;
    private long frameTime;

    public Coords getMouse() {
        return mouse;
    }

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