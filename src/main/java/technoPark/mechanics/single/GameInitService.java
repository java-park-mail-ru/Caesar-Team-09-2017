package technoPark.mechanics.single;

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
import static technoPark.mechanics.Config.*;
import static technoPark.mechanics.Config.GROUND_HEIGHT;
import static technoPark.mechanics.Config.MAP;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapshotService.class);

    @NotNull
    private final RemotePointService remotePointService;

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull technoPark.mechanics.GameSession gameSession) {
        final GameUser player = gameSession.getPlayer();

        final InitGameSinglePlayer.Response initMessage = createInitMessageFor(gameSession, player.getAccountId());
        //noinspection OverlyBroadCatchBlock
        try {
            remotePointService.sendMessageToUser(player.getAccountId(), initMessage);
        } catch (IOException e) {
            //TODO: Reentrance mechanism
           remotePointService.cutDownConnection(player.getAccountId(), CloseStatus.SERVER_ERROR);
            LOGGER.error("Unnable to start a models", e);
        }

    }

    @SuppressWarnings("TooBroadScope")
    private InitGameSinglePlayer.Response createInitMessageFor(@NotNull technoPark.mechanics.GameSession gameSession, @NotNull Id<AccountDao> userId) {
        final InitGameSinglePlayer.Response initGameSinglePlayerMessage = new InitGameSinglePlayer.Response();

        final Map<Id<AccountDao>, GameUser.ServerPlayerSnap> playerSnaps = new HashMap<>();
        final Map<Id<AccountDao>, String> names = new HashMap<>();
        final Map<Id<AccountDao>, String> colors = new HashMap<>();

        final GameUser player = gameSession.getPlayer();

        playerSnaps.put(player.getAccountId(), player.getSnap());
        names.put(player.getAccountId(), player.getAccountDao().getUsername());

        colors.put(userId, Config.SELF_COLOR);

        initGameSinglePlayerMessage.setWorldWidth(WORLD_WIDTH);
        initGameSinglePlayerMessage.setWorldHeight(WORLD_HEIGHT);
        initGameSinglePlayerMessage.setPlayerX(PLAYER_X);
        initGameSinglePlayerMessage.setPlayerY(PLAYER_Y);
        initGameSinglePlayerMessage.setPlayerWidth(PLAYER_WIDTH);
        initGameSinglePlayerMessage.setPlayerHeight(PLAYER_HEIGHT);
        initGameSinglePlayerMessage.setCoins(COINS);
        initGameSinglePlayerMessage.setCoinWidth(COIN_WIDTH);
        initGameSinglePlayerMessage.setCoinHeight(COINT_HEIGHT);
        initGameSinglePlayerMessage.setGroundWidth(GROUND_WIDTH);
        initGameSinglePlayerMessage.setGroundHeight(GROUND_HEIGHT);
        initGameSinglePlayerMessage.setMap(MAP);
//        initGameSinglePlayerMessage.setBoard(gameSession.getBoard().getSnap());
        return initGameSinglePlayerMessage;
    }
}
