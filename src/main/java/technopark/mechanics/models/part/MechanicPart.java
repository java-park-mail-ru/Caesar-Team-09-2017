package technopark.mechanics.models.part;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.Config;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.Snap;

import static technopark.mechanics.Config.START_ENERGY;

public class MechanicPart implements GamePart {

    private int energy;

    private boolean isDrill;
    private long lastTimeDrilled;
    private boolean isMove;
    private long lastTimeMoved;

    @NotNull
    private final MechanicsTimeService timeService;


    public MechanicPart(@NotNull MechanicsTimeService timeService) {
        this.timeService = timeService;
        energy = START_ENERGY;
        lastTimeDrilled = -Config.DRILING_COOLDOWN;
        lastTimeMoved = -Config.MOVEMENT_COOLDOWN;
        isDrill = false;
    }

    public int getEnergy() {
        return energy;
    }

    public void decrementEnergy() {
        this.energy--;
    }

    public boolean tryDrill() {
        if (isDrill) {
            return false;
        }
        final long now = timeService.time();
        if (lastTimeMoved + Config.MOVEMENT_COOLDOWN <= now) {
            lastTimeMoved = now;
            isDrill = true;
            return true;
        }
        return false;
    }

    public boolean tryMove() {
        if (isMove) {
            return false;
        }
        final long now = timeService.time();
        if (lastTimeDrilled + Config.DRILING_COOLDOWN <= now) {
            lastTimeDrilled = now;
            isMove = true;
            return true;
        }
        return false;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    public void setDrill(boolean drill) {
        isDrill = drill;
    }

    @Override
    public MechanicPartSnap takeSnap() {
        return new MechanicPartSnap(this);
    }

    public static final class MechanicPartSnap implements Snap<MechanicPart> {

        private final int score;
        private final boolean isDrill;

        public MechanicPartSnap(MechanicPart mechanicPart) {
            this.score = mechanicPart.energy;
            this.isDrill = mechanicPart.isDrill;
        }

        public int getScore() {
            return score;
        }

        public boolean isDrill() {
            return isDrill;
        }
    }
}
