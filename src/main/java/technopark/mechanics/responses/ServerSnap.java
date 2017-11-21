package technopark.mechanics.responses;

import technopark.mechanics.models.MapForGame;
import technopark.mechanics.models.part.MechanicPart;
import technopark.websocket.MessageResponse;

@SuppressWarnings({"NullableProblems"})
public class ServerSnap extends MessageResponse {

    private long serverFrameTime;

    private MechanicPart.MechanicPartSnap mechanicPartSnap;
    private MapForGame.MapSnap mapSnap;

    public MechanicPart.MechanicPartSnap getMechanicPartSnap() {
        return mechanicPartSnap;
    }

    public void setMechanicPartSnap(MechanicPart.MechanicPartSnap mechanicPartSnap) {
        this.mechanicPartSnap = mechanicPartSnap;
    }

    public MapForGame.MapSnap getMapSnap() {
        return mapSnap;
    }

    public void setMapSnap(MapForGame.MapSnap mapSnap) {
        this.mapSnap = mapSnap;
    }

    public long getServerFrameTime() {
        return serverFrameTime;
    }

    public void setServerFrameTime(long serverFrameTime) {
        this.serverFrameTime = serverFrameTime;
    }

}
