package technoPark.mechanics.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import technoPark.mechanics.Config;
import technoPark.mechanics.models.player.GameUser;
import technoPark.mechanics.models.session.GameSession;
import technoPark.mechanics.responses.InitGameMultiPlayer;
import technoPark.mechanics.responses.InitGameSinglePlayer;
import technoPark.mechanics.services.snap.ServerSnapshotService;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;
import technoPark.websocket.RemotePointService;
import static technoPark.mechanics.Config.*;
import static technoPark.mechanics.Config.GROUND_HEIGHT;
import static technoPark.mechanics.Config.MAP;

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

    public void initGameFor(@NotNull GameSession gameSession) {
        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        if (!gameSession.isSinglePlay()) {
            players.add(gameSession.getSecond());
        }
        for (GameUser player : players) {
            //noinspection OverlyBroadCatchBlock
            try {
                if (!gameSession.isSinglePlay()) {
                    remotePointService.sendMessageToUser(player.getAccountId(), createInitMessageForMulti(gameSession, player.getAccountId()));
                } else {
                    remotePointService.sendMessageToUser(player.getAccountId(), createInitMessageForSingle(gameSession, player.getAccountId()));
                }
            } catch (IOException e) {
                //TODO: Reentrance mechanism
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getAccountId(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a models", e);
            }
        }
    }

    @SuppressWarnings("TooBroadScope")
    private InitGameSinglePlayer.Response createInitMessageForSingle(@NotNull GameSession gameSession, @NotNull Id<AccountDao> userId) {
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
        initGameSinglePlayerMessage.setCountOfBonuses(COUNT_OF_BONUSES);
        initGameSinglePlayerMessage.setCoinWidth(COIN_WIDTH);
        initGameSinglePlayerMessage.setCoinHeight(COINT_HEIGHT);
        initGameSinglePlayerMessage.setGroundWidth(GROUND_WIDTH);
        initGameSinglePlayerMessage.setGroundHeight(GROUND_HEIGHT);
        initGameSinglePlayerMessage.setMap(MAP);
        initGameSinglePlayerMessage.setStartMoney(START_MONEY);
        initGameSinglePlayerMessage.setStartEnergy(START_ENERGY);
//        initGameSinglePlayerMessage.setBoard(gameSession.getMapForGame().getSnap());
        return initGameSinglePlayerMessage;
    }

    @SuppressWarnings("TooBroadScope")
    private InitGameMultiPlayer.Response createInitMessageForMulti(@NotNull GameSession gameSession, @NotNull Id<AccountDao> userId) {
        final InitGameMultiPlayer.Response initGameSinglePlayerMessage = new InitGameMultiPlayer.Response();

        return initGameSinglePlayerMessage;
    }
}
