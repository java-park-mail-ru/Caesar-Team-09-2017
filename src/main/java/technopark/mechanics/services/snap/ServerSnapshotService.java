package technopark.mechanics.services.snap;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.responses.ServerSnap;
import technopark.websocket.RemotePointService;


import java.io.IOException;

@Service
public class ServerSnapshotService {
    @NotNull
    private final RemotePointService remotePointService;

    public ServerSnapshotService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull GameSession gameSession, long frameTime) {
        final ServerSnap snap = new ServerSnap();
        snap.setServerFrameTime(frameTime);
        snap.setMapSnap(gameSession.getMapForGame().getSnap());
        snap.setMechanicPartSnap(gameSession.getFirst().claimPart(MechanicPart.class).takeSnap());
        //noinspection OverlyBroadCatchBlock
        try {
            for (GameUser player : gameSession.getPlayers()) {
                remotePointService.sendMessageToUser(player.getAccountId(), snap);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }
}
