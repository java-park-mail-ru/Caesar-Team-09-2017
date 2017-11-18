package technoPark.mechanics.multi;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import technoPark.mechanics.Config;
import technoPark.mechanics.ServerSnapshotService;
import technoPark.mechanics.models.GameUser;
import technoPark.mechanics.responses.InitGameSinglePlayer;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;
import technoPark.websocket.RemotePointService;

import java.io.IOException;
import java.util.*;

@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapshotService.class);

    @NotNull
    private final RemotePointService remotePointService;

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull technoPark.mechanics.GameSession gameSession) {
        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            final InitGameSinglePlayer.Response initMessage = createInitMessageFor(gameSession, player.getAccountId());
            //noinspection OverlyBroadCatchBlock
            try {
                remotePointService.sendMessageToUser(player.getAccountId(), initMessage);
            } catch (IOException e) {
                //TODO: Reentrance mechanism
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getAccountId(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a models", e);
            }
        }
    }

    @SuppressWarnings("TooBroadScope")
    private InitGameSinglePlayer.Response createInitMessageFor(@NotNull technoPark.mechanics.GameSession gameSession, @NotNull Id<AccountDao> userId) {
        final InitGameSinglePlayer.Response initGameSinglePlayerMessage = new InitGameSinglePlayer.Response();

        final Map<Id<AccountDao>, GameUser.ServerPlayerSnap> playerSnaps = new HashMap<>();
        final Map<Id<AccountDao>, String> names = new HashMap<>();
        final Map<Id<AccountDao>, String> colors = new HashMap<>();

        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            playerSnaps.put(player.getAccountId(), player.getSnap());
            names.put(player.getAccountId(), player.getAccountDao().getUsername());
        }

        colors.put(userId, Config.SELF_COLOR);
        colors.put(gameSession.getEnemy(userId).getAccountId(), Config.ENEMY_COLOR);


//        initGameSinglePlayerMessage.setBoard(gameSession.getBoard().getSnap());
        return initGameSinglePlayerMessage;
    }
}
