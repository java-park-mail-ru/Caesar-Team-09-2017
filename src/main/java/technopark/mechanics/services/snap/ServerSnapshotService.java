package technopark.mechanics.services.snap;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.part.PositionPart;
import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.responses.ServerSnap;
import technopark.mechanics.models.ServerSnapUser;
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

        final ServerSnapUser firstServerSnapUser = new ServerSnapUser();

        firstServerSnapUser.setMechanicPartSnap(gameSession.getFirst().claimPart(MechanicPart.class).takeSnap());
        firstServerSnapUser.setPositionPartSnap(gameSession.getFirst().claimPart(PositionPart.class).takeSnap());
        firstServerSnapUser.setUserId(gameSession.getFirst().getAccountId().getId());
        snap.setFirstUser(firstServerSnapUser);
        if (!gameSession.isSinglePlay()) {
            final ServerSnapUser secondServerSnapUser = new ServerSnapUser();

            secondServerSnapUser.setMechanicPartSnap(gameSession.getSecond().claimPart(MechanicPart.class).takeSnap());
            secondServerSnapUser.setPositionPartSnap(gameSession.getSecond().claimPart(PositionPart.class).takeSnap());
            secondServerSnapUser.setUserId(gameSession.getSecond().getAccountId().getId());
            snap.setSecondUser(secondServerSnapUser);
        }
        //noinspection OverlyBroadCatchBlock
        try {
            for (GameUser player : gameSession.getPlayers()) {
                if (!player.isShopping()) {
                    remotePointService.sendMessageToUser(player.getAccountId(), snap);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }
}
