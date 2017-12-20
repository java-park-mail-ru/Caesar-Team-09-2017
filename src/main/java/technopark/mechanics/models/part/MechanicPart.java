package technopark.mechanics.models.part;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.Config;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.Cooldown;
import technopark.mechanics.models.Snap;

import static technopark.mechanics.Config.*;

public class MechanicPart implements GamePart {

    private int energy;
    private int money;
    private Cooldown drill;
    private Cooldown move;
    private Cooldown jump;

    private int startDayEnergy;
    private int drillPower;
    private int radiusRadar;

    @NotNull
    private final MechanicsTimeService timeService;

    public MechanicPart(@NotNull MechanicsTimeService timeService) {
        this.timeService = timeService;
        startDayEnergy = START_ENERGY;
        money = START_MONEY;
        radiusRadar = RADIUS_RADAR;
        drill = new Cooldown();
        move = new Cooldown();
        jump = new Cooldown();
        drill.is = false;
        move.is = false;
        jump.is = false;
        drill.lastTime = -Config.DRILING_COOLDOWN;
        move.lastTime = -Config.MOVEMENT_COOLDOWN;
        jump.lastTime = -Config.JUMPING_COOLDOWN;

        this.setEnergyLakiStartDay();
        drillPower = 2;
    }

    public void incrStartDayEnergy() {
        this.startDayEnergy += 20;
        this.money -= COST_UPGRADE_ENERGY;
    }

    public void incrDrillPower() {
        this.drillPower += 1;
        this.money -= COST_UPGRADE_DRILL;
    }

    public void incrRadiusRadar() {
        this.radiusRadar += 100;
        this.money -= COST_UPGRADE_RADAR;
    }

    public void setEnergyLakiStartDay() {
        this.energy = this.startDayEnergy;
        System.out.println(energy);
    }

    public int getDrillPower() {
        return drillPower;
    }

    public void decrementEnergy() {
        this.energy--;
    }

    public void changeMoney(int value) {
        this.money += value;
    }

    private boolean cooldownService(Cooldown something, long sometingCooldown) {
        final long now = timeService.time();
        if (something.lastTime + sometingCooldown <= now) {
            something.lastTime = now;
            something.is = true;
            return true;
        }
        return false;
    }

    public boolean tryDrill() {
        return cooldownService(drill, Config.DRILING_COOLDOWN);
    }

    public boolean tryMove() {
        return cooldownService(move, Config.MOVEMENT_COOLDOWN);
    }

    public boolean tryJump() {
        return cooldownService(jump, Config.JUMPING_COOLDOWN);
    }

    public void setDrill(boolean flag) {
        drill.is = flag;
    }

    public void setMove(boolean flag) {
        move.is = flag;
    }


    @Override
    public MechanicPartSnap takeSnap() {
        return new MechanicPartSnap(this);
    }

    public static final class MechanicPartSnap implements Snap<MechanicPart> {

        private final int energy;
        private final int money;
        private final int radiusRadar;

        public MechanicPartSnap(MechanicPart mechanicPart) {
            this.energy = mechanicPart.energy;
            this.money = mechanicPart.money;
            this.radiusRadar = mechanicPart.radiusRadar;
        }

        public int getEnergy() {
            return energy;
        }

        public int getMoney() {
            return money;
        }

        public int getRadiusRadar() {
            return radiusRadar;
        }
    }
}
