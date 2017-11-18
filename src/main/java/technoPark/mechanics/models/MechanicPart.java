package technoPark.mechanics.models;

import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.Config;
import technoPark.mechanics.MechanicsTimeService;

public class MechanicPart implements GamePart {
    private int score;
    private boolean isDrill;
    private long lastTimeDrilled;

    @NotNull
    private final MechanicsTimeService timeService;


    public MechanicPart(@NotNull MechanicsTimeService timeService) {
        this.timeService = timeService;
        score = 0;
        lastTimeDrilled = -Config.DRILING_COOLDOWN;
        isDrill = false;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    public boolean tryFire() {
        if (isDrill) {
            return false;
        }
        final long now = timeService.time();
        if (lastTimeDrilled + Config.DRILING_COOLDOWN <= now) {
            lastTimeDrilled = now;
            isDrill = true;
            return true;
        }
        return false;
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
            this.score = mechanicPart.score;
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
