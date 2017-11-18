package technoPark.mechanics.responses;

import technoPark.mechanics.models.GameUser;
import org.jetbrains.annotations.NotNull;
import technoPark.websocket.MessageResponse;

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
