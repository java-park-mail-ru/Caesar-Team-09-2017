package technopark.mechanics.models;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.part.PositionPart;

public class ServerSnapUser {
    @NotNull
    private long userId;
    @NotNull
    private MechanicPart.MechanicPartSnap mechanicPartSnap;
    @NotNull
    private PositionPart.PositionSnap positionPartSnap;

    public PositionPart.PositionSnap getPositionPartSnap() {
        return positionPartSnap;
    }

    public void setPositionPartSnap(PositionPart.PositionSnap positionSnapSnap) {
        this.positionPartSnap = positionSnapSnap;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public MechanicPart.MechanicPartSnap getMechanicPartSnap() {
        return mechanicPartSnap;
    }

    public void setMechanicPartSnap(MechanicPart.MechanicPartSnap mechanicPartSnap) {
        this.mechanicPartSnap = mechanicPartSnap;
    }
}
