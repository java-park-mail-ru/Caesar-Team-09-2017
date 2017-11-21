package technopark.mechanics.services.snap;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import technopark.mechanics.models.Coords;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.responses.ServerSnap;
import technopark.websocket.RemotePointService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerSnapshotService {
    @NotNull
    private final RemotePointService remotePointService;

    public ServerSnapshotService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull GameSession gameSession, long frameTime) {
        // TODO: предусмотреть еще и для мультиплеера
//        final List<GameUser.ServerPlayerSnap> playersSnaps = new ArrayList<>();
//        for (GameUser player : gameSession.getPlayers()) {
//            playersSnaps.add(player.getSnap());
//        }
        final ServerSnap snap = new ServerSnap();
        snap.setServerFrameTime(frameTime);
//        snap.setPlayer(gameSession.getFirst().getSnap());
        snap.setMoveDifference(gameSession.getMapForGame().getSnap().getMoveDifference().get(0));
        snap.setDestroyedTiles(gameSession.getFirst().claimPart(MechanicPart.class).takeSnap().getDestroyedTiles());
        snap.setDrilledSuccessful(gameSession.getFirst().claimPart(MechanicPart.class).takeSnap().isDrill());
        snap.setEnergyDifference(gameSession.getFirst().claimPart(MechanicPart.class).takeSnap().getDiffEnergy());
        snap.setMoneyDifference(gameSession.getFirst().claimPart(MechanicPart.class).takeSnap().getDiffMoney());
        // snap.setBoard(gameSession.getMapForGame().getSnap());
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
