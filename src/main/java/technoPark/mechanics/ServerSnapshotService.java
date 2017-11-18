package technoPark.mechanics;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import technoPark.mechanics.models.GameUser;
import technoPark.mechanics.multi.GameSession;
import technoPark.mechanics.responses.ServerSnap;
import technoPark.websocket.RemotePointService;


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
        final List<GameUser.ServerPlayerSnap> playersSnaps = new ArrayList<>();
        for (GameUser player : gameSession.getPlayers()) {
            playersSnaps.add(player.getSnap());
        }
        final ServerSnap snap = new ServerSnap();

        snap.setPlayers(playersSnaps);
//        snap.setBoard(gameSession.getBoard().getSnap());
        snap.setServerFrameTime(frameTime);
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
