package technopark.mechanics;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.requests.ClientSnap;
import technopark.mechanics.responses.InitGameSinglePlayer;
import static technopark.mechanics.Config.*;

import java.io.IOException;

@SuppressWarnings("MagicNumber")
public class SnapSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private GameUser nogibator;
    private GameUser papkaPro;
    private AccountDao pupkin;
    private AccountDao dudkin;

    @Before
    public void setUp() {
        pupkin = new AccountDao("pupkin@mail.ru", "pupkin", "123");
        dudkin = new AccountDao("dupkin@mail.ru", "dupnik", "456");
        nogibator = new GameUser(pupkin, new MechanicsTimeService());
        papkaPro = new GameUser(dudkin, new MechanicsTimeService());
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void clientSnapTest() throws IOException {
        final String clientSnapStr =
                    "{ " +
                        "\"mouse\":{" +
                            "\"x\":34," +
                            "\"y\":55" +
                        "}," +
                        "\"isJump\":\"false\"," +
                        "\"isMove\":\"true\"," +
                        "\"isDrill\":\"false\"," +
                        "\"class\":\"ClientSnap\"," +
                        "\"moveTo\":{\"keyDown\":\"RIGHT\"}," +
                        "\"frameTime\":\"32\"" +
                    '}';

        final ClientSnap clientSnap = objectMapper.readValue(clientSnapStr, ClientSnap.class);
        final String clientSnapJson = objectMapper.writeValueAsString(clientSnap);
        Assert.assertNotNull(clientSnapJson);
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void serverSnapTest() throws IOException {
        final GameUser.ServerPlayerSnap serverPlayerSnap = new GameUser.ServerPlayerSnap();
        serverPlayerSnap.setUserId(Id.of(4));
        final String result = objectMapper.writeValueAsString(serverPlayerSnap);
        objectMapper.readValue(result, GameUser.ServerPlayerSnap.class);
    }

    @SuppressWarnings({"TooBroadScope", "OverlyBroadThrowsClause"})
    @Test
    public void serverInitSingleTest() throws IOException {
        final InitGameSinglePlayer.Response initGameSinglePlayerMessage = new InitGameSinglePlayer.Response();
        final AccountDao pupkin = new AccountDao("pupkin@mail.ru", "pupkin", "123");
        final GameUser gameUser = new GameUser(pupkin, new MechanicsTimeService());
        final GameUser.ServerPlayerSnap serverPlayerSnap = this.nogibator.getSnap();
        initGameSinglePlayerMessage.setWorldWidth(WORLD_WIDTH);
        initGameSinglePlayerMessage.setWorldHeight(WORLD_HEIGHT);
        initGameSinglePlayerMessage.setPlayerX(PLAYER_X[0]);
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
        initGameSinglePlayerMessage.setUserId(pupkin.getIdLong());
        initGameSinglePlayerMessage.setRadiusRadar(RADIUS_RADAR);
        final String initGameJson = objectMapper.writeValueAsString(initGameSinglePlayerMessage);
        Assert.assertNotNull(initGameJson);
    }

}
