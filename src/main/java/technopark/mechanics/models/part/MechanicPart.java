package technopark.mechanics.models.part;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.Config;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.Coords;
import technopark.mechanics.models.Snap;

import static technopark.mechanics.Config.START_ENERGY;
import static technopark.mechanics.Config.START_MONEY;

public class MechanicPart implements GamePart {

    private int energy;
    private int money;
    private boolean isDrill;
    private long lastTimeDrilled;
    private boolean isMove;
    private long lastTimeMoved;

    @NotNull
    private final MechanicsTimeService timeService;

    public MechanicPart(@NotNull MechanicsTimeService timeService) {
        this.timeService = timeService;
        lastTimeDrilled = -Config.DRILING_COOLDOWN;
        lastTimeMoved = -Config.MOVEMENT_COOLDOWN;
        energy = START_ENERGY;
        money = START_MONEY;
        isDrill = false;
        isMove = false;
    }

    public void decrementEnergy() {
        this.energy--;
    }

    public void changeMoney(int value) {
        this.money += value;
    }

    public boolean tryDrill() {
//        if (isDrill) {
//            return false;
//        }
        System.out.println("tryDrill");
        final long now = timeService.time();
        if (lastTimeDrilled + Config.DRILING_COOLDOWN <= now) {
            lastTimeDrilled = now;
            isDrill = true;
            return true;
        }
        return false;
    }

    public boolean tryMove() {
//        if (isMove) {
//            return false;
//        }
        final long now = timeService.time();
        if (lastTimeMoved + Config.MOVEMENT_COOLDOWN <= now) {
            lastTimeMoved = now;
            isMove = true;
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

        private final int energy;
        private final int money;
        private final boolean isDrill;

        public MechanicPartSnap(MechanicPart mechanicPart) {
            this.energy = mechanicPart.energy;
            this.money = mechanicPart.money;
            this.isDrill = mechanicPart.isDrill;
        }

        public int getEnergy() {
            return energy;
        }

        public int getMoney() {
            return money;
        }

    }
}
