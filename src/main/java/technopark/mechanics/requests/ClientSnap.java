package technopark.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import technopark.mechanics.models.Coords;
import technopark.mechanics.models.Move;
import technopark.websocket.MessageRequest;

@SuppressWarnings({"NullableProblems"})
public class ClientSnap extends MessageRequest {

    private Coords mouse;
    private Coords bonus;
    private Move moveTo;

    private boolean isDrill;
    private boolean isMove;
    private boolean isJump;
    private boolean isBonus;

    private long frameTime;

    public Coords getBonus() {
        return bonus;
    }

    public void setBonus(Coords bonus) {
        this.bonus = bonus;
    }

    public boolean isBonus() {
        return isBonus;
    }

    @JsonProperty("isBonus")
    public void isBonus(boolean bonus) {
        isBonus = bonus;
    }

    public boolean isJump() {
        return isJump;
    }

    @JsonProperty("isJump")
    public void setJump(boolean jump) {
        isJump = jump;
    }

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

    @JsonProperty("isDrill")
    public void setDrill(boolean drill) {
        isDrill = drill;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }

    public boolean isMove() {
        return isMove;
    }

    @JsonProperty("isMove")
    public void setMove(boolean move) {
        isMove = move;
    }

    public Move getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(Move moveTo) {
        this.moveTo = moveTo;
    }

}