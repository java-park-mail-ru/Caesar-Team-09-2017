package technopark.mechanics.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.responses.InitGameMultiPlayer;
import technopark.mechanics.responses.InitGameSinglePlayer;
import technopark.mechanics.services.snap.ServerSnapshotService;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;
import technopark.websocket.RemotePointService;

import static technopark.mechanics.Config.*;

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
                    remotePointService.sendMessageToUser(player.getAccountId(),
                            createInitMessageForMulti(gameSession, player.getAccountId()));
                } else {
                    remotePointService.sendMessageToUser(player.getAccountId(),
                            createInitMessageForSingle(gameSession, player.getAccountId()));
                }
            } catch (IOException e) {
                // TODO : Reentrance mechanism
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getAccountId(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a models", e);
            }
        }
    }

    @SuppressWarnings("TooBroadScope")
    private InitGameSinglePlayer.Response createInitMessageForSingle(@NotNull GameSession gameSession, @NotNull Id<AccountDao> userId) {
        final InitGameSinglePlayer.Response initGameSinglePlayerMessage = new InitGameSinglePlayer.Response();

        initGameSinglePlayerMessage.setWorldWidth(WORLD_WIDTH);
        initGameSinglePlayerMessage.setWorldHeight(WORLD_HEIGHT);
        initGameSinglePlayerMessage.setPlayerX(PLAYER_X);
        initGameSinglePlayerMessage.setPlayerY(PLAYER_Y);
        initGameSinglePlayerMessage.setPlayerWidth(PLAYER_WIDTH);
        initGameSinglePlayerMessage.setPlayerHeight(PLAYER_HEIGHT);
        initGameSinglePlayerMessage.setCountOfBonuses(COUNT_OF_BONUSES);
        initGameSinglePlayerMessage.setCoinWidth(COIN_WIDTH);
        initGameSinglePlayerMessage.setCoinHeight(COIN_HEIGHT);
        initGameSinglePlayerMessage.setGroundWidth(GROUND_WIDTH);
        initGameSinglePlayerMessage.setGroundHeight(GROUND_HEIGHT);
        initGameSinglePlayerMessage.setStartMoney(START_MONEY);
        initGameSinglePlayerMessage.setStartEnergy(START_ENERGY);
        initGameSinglePlayerMessage.setPositionGround(POSITION_GROUND);
        initGameSinglePlayerMessage.setBonusPosition(BONUS_POSITION);
        initGameSinglePlayerMessage.setUserId(userId.getId());
        initGameSinglePlayerMessage.setRadiusRadar(RADIUS_RADAR);

        return initGameSinglePlayerMessage;
    }

    @SuppressWarnings("TooBroadScope")
    private InitGameMultiPlayer.Response createInitMessageForMulti(@NotNull GameSession gameSession, @NotNull Id<AccountDao> userId) {
        final InitGameMultiPlayer.Response initGameMultiPlayerMessage = new InitGameMultiPlayer.Response();

        initGameMultiPlayerMessage.setWorldWidth(WORLD_WIDTH);
        initGameMultiPlayerMessage.setWorldHeight(WORLD_HEIGHT);
        initGameMultiPlayerMessage.setPlayerX(PLAYER_X);
        initGameMultiPlayerMessage.setPlayerY(PLAYER_Y);
        initGameMultiPlayerMessage.setPlayerWidth(PLAYER_WIDTH);
        initGameMultiPlayerMessage.setPlayerHeight(PLAYER_HEIGHT);
        initGameMultiPlayerMessage.setCountOfBonuses(COUNT_OF_BONUSES);
        initGameMultiPlayerMessage.setCoinWidth(COIN_WIDTH);
        initGameMultiPlayerMessage.setCoinHeight(COIN_HEIGHT);
        initGameMultiPlayerMessage.setGroundWidth(GROUND_WIDTH);
        initGameMultiPlayerMessage.setGroundHeight(GROUND_HEIGHT);
        initGameMultiPlayerMessage.setStartMoney(START_MONEY);
        initGameMultiPlayerMessage.setStartEnergy(START_ENERGY);
        initGameMultiPlayerMessage.setPositionGround(POSITION_GROUND);
        initGameMultiPlayerMessage.setBonusPosition(BONUS_POSITION);
        initGameMultiPlayerMessage.setUserId(userId.getId());
        initGameMultiPlayerMessage.setRadiusRadar(RADIUS_RADAR);
        initGameMultiPlayerMessage.setOtherUserId(gameSession.getEnemy(userId).getAccountId().getId());

        return initGameMultiPlayerMessage;
    }
}
