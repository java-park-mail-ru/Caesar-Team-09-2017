package technopark.mechanics.responses;

import org.jetbrains.annotations.NotNull;
import technopark.mechanics.models.MapForGame;
import technopark.mechanics.models.ServerSnapUser;
import technopark.websocket.MessageResponse;

@SuppressWarnings({"NullableProblems"})
public class ServerSnap extends MessageResponse {

    @NotNull
    private long serverFrameTime;
    @NotNull
    private MapForGame.MapSnap mapSnap;

    @NotNull
    private ServerSnapUser firstUser;

    private ServerSnapUser secondUser;

    public ServerSnapUser getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(ServerSnapUser firstUser) {
        this.firstUser = firstUser;
    }

    public ServerSnapUser getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(ServerSnapUser secondUser) {
        this.secondUser = secondUser;
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
