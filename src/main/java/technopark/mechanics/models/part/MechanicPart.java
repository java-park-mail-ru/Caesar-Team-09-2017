package technopark.mechanics.models.part;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.Config;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.Snap;

import static technopark.mechanics.Config.START_ENERGY;
import static technopark.mechanics.Config.START_MONEY;

public class MechanicPart implements GamePart {

    private int currentEnergy;
    private int currentMoney;
    private int prevEnergy;
    private int prevMoney;
    private boolean isDrill;
    private long lastTimeDrilled;
    private boolean isMove;
    private long lastTimeMoved;

    @NotNull
    private final MechanicsTimeService timeService;


    public MechanicPart(@NotNull MechanicsTimeService timeService) {
        this.timeService = timeService;
        prevEnergy = currentEnergy = START_ENERGY;
        prevMoney = currentMoney = START_MONEY;
        lastTimeDrilled = -Config.DRILING_COOLDOWN;
        lastTimeMoved = -Config.MOVEMENT_COOLDOWN;
        isDrill = false;
    }

    public void decrementEnergy() {
        this.prevEnergy = this.currentEnergy;
        this.currentEnergy--;
    }

    public void changeMoney(int value) {
        this.prevMoney = this.currentMoney;
        this.currentMoney += value;
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
        private final int diffEnergy;
        private final int diffMoney;
        private final boolean isDrill;

        public MechanicPartSnap(MechanicPart mechanicPart) {
            this.energy = mechanicPart.currentEnergy;
            this.diffEnergy = mechanicPart.currentEnergy - mechanicPart.prevEnergy;
            this.money = mechanicPart.currentMoney;
            this.diffMoney = mechanicPart.currentMoney - mechanicPart.prevMoney;
            this.isDrill = mechanicPart.isDrill;
        }

        public int getEnergy() {
            return energy;
        }

        public boolean isDrill() {
            return isDrill;
        }

        public int getMoney() {
            return money;
        }

        public int getDiffEnergy() {
            return diffEnergy;
        }

        public int getDiffMoney() {
            return diffMoney;
        }
    }
}
