package technopark.mechanics.responses;

import technopark.mechanics.models.player.GameUser;
import org.jetbrains.annotations.NotNull;
import technopark.websocket.MessageResponse;

import java.util.List;

@SuppressWarnings({"NullableProblems"})
public class ServerSnap extends MessageResponse {

    @NotNull
    private List<GameUser.ServerPlayerSnap> players;
    private long serverFrameTime;

    @NotNull
    public List<GameUser.ServerPlayerSnap> getPlayers() {
        return players;
    }

    public void setPlayers(@NotNull List<GameUser.ServerPlayerSnap> players) {
        this.players = players;
    }

    public long getServerFrameTime() {
        return serverFrameTime;
    }

    public void setServerFrameTime(long serverFrameTime) {
        this.serverFrameTime = serverFrameTime;
    }
}
