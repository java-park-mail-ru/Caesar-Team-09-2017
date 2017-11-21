package technopark.mechanics.responses;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.models.Coords;
import technopark.mechanics.models.player.GameUser;
import technopark.websocket.MessageResponse;

import java.util.List;

@SuppressWarnings({"NullableProblems"})
public class ServerSnap extends MessageResponse {

//    @NotNull
//    private List<GameUser.ServerPlayerSnap> players;
    private GameUser.ServerPlayerSnap player;
    private long serverFrameTime;
    private Coords moveDifference;
    private int energyDifference;
    private int moneyDifference;
    private boolean isDrilledSuccessful;
    private Coords[] destroyedTiles;

    public Coords[] getDestroyedTiles() {
        return destroyedTiles;
    }

    public void setDestroyedTiles(Coords[] destroyedTiles) {
        this.destroyedTiles = destroyedTiles;
    }

    public boolean isDrilledSuccessful() {
        return isDrilledSuccessful;
    }

    public void setDrilledSuccessful(boolean drilledSuccessful) {
        isDrilledSuccessful = drilledSuccessful;
    }

    public int getEnergyDifference() {
        return energyDifference;
    }

    public void setEnergyDifference(int energyDifference) {
        this.energyDifference = energyDifference;
    }

    public int getMoneyDifference() {
        return moneyDifference;
    }

    public void setMoneyDifference(int moneyDifference) {
        this.moneyDifference = moneyDifference;
    }

    public Coords getMoveDifference() {
        return moveDifference;
    }

    public void setMoveDifference(Coords moveDifference) {
        this.moveDifference = moveDifference;
    }

    public long getServerFrameTime() {
        return serverFrameTime;
    }

    public void setServerFrameTime(long serverFrameTime) {
        this.serverFrameTime = serverFrameTime;
    }

    public GameUser.ServerPlayerSnap getPlayer() {
        return player;
    }

    public void setPlayer(GameUser.ServerPlayerSnap player) {
        this.player = player;
    }
}
