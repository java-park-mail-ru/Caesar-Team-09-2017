/*package technopark.mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import technopark.account.Account;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.Coords;
import technopark.mechanics.models.Move;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.session.GameSession;
import technopark.mechanics.requests.ClientSnap;
import technopark.mechanics.requests.JoinGame;
import technopark.mechanics.services.session.GameSessionService;
import technopark.services.AccountService;
import technopark.websocket.RemotePointService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings({"MagicNumber", "NullableProblems", "SpringJavaAutowiredMembersInspection"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class GameMechanicsTest {

    @SuppressWarnings("unused")
    @MockBean
    private RemotePointService remotePointService;
    @SuppressWarnings("unused")
    @MockBean
    private MechanicsExecutor mechanicsExecutor;
    @Autowired
    private GameMechanics gameMechanics;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GameSessionService gameSessionService;
    @NotNull
    private AccountDao user1;
    @NotNull
    private AccountDao user2;

    @Before
    public void setUp () {
        when(remotePointService.isConnected(any())).thenReturn(true);
        user1 = accountService.createAccount(new Account("user1@mail.ru", "user1", "123"));
        user1.setId(1);
        user2 = accountService.createAccount(new Account("user2@mail.ru", "user2", "456"));
        user2.setId(2);
    }

    @Test
    public void simpleDrilling() {
        final GameSession gameSession = startGame(new Id<AccountDao>(user1.getIdLong()), new Id<AccountDao>(user2.getIdLong()));
        gameMechanics.addClientSnapshot(gameSession.getFirst().getAccountId(), createClientSnap(25,true, Coords.of(Config.PLAYER_X, Config.PLAYER_Y + Config.PLAYER_HEIGHT)));
        gameMechanics.addClientSnapshot(gameSession.getSecond().getAccountId(), createClientSnap(40, true, Coords.of(0,0)));
        gameMechanics.gmStep(100);
        Assert.assertEquals(Config.START_ENERGY - 1, gameSession.getFirst().claimPart(MechanicPart.class).takeSnap().getEnergy());
        Assert.assertEquals(Config.START_ENERGY, gameSession.getSecond().claimPart(MechanicPart.class).takeSnap().getEnergy());
    }

    @Test
    public void firingTooFastTest() {

        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getSecond().getAccountId(), createClientSnap(25,true, Coords.of(Config.PLAYER_X + Config.PLAYER_WIDTH, Config.PLAYER_Y)));
        gameMechanics.gmStep(50);
        gameMechanics.addClientSnapshot(gameSession.getSecond().getAccountId(), createClientSnap(25,true, Coords.of(Config.PLAYER_X + Config.PLAYER_WIDTH, Config.PLAYER_Y)));
        gameMechanics.gmStep(50);
        gameMechanics.addClientSnapshot(gameSession.getSecond().getAccountId(), createClientSnap(25,true, Coords.of(Config.PLAYER_X + Config.PLAYER_WIDTH, Config.PLAYER_Y)));
        gameMechanics.gmStep(50);
        Assert.assertEquals(Config.START_ENERGY - 1, gameSession.getSecond().claimPart(MechanicPart.class).takeSnap().getEnergy());
    }

    @Test
    public void gameStartedTest () {
        startGame(user1.getId(), user2.getId());
    }

    @SuppressWarnings("SameParameterValue")
    private ClientSnap createClientSnap(long frameTime, boolean drilling, Coords mouse) {
        final ClientSnap clientSnap = new ClientSnap();
        clientSnap.setFrameTime(frameTime);
        clientSnap.setMouse(mouse);
        if (drilling) {
            clientSnap.setDrill(true);
            clientSnap.setMove(false);
            clientSnap.setMoveTo(new Move(Config.KeyDown.NOTHING));
        } else {
            clientSnap.setDrill(false);
            clientSnap.setMove(true);
            clientSnap.setMoveTo(new Move(Config.KeyDown.LEFT));
        }

        return clientSnap;
    }

    @NotNull
    private GameSession startGame(@NotNull Id<AccountDao> player1, @NotNull Id<AccountDao> player2) {
        JoinGame joinGame = new JoinGame();
        joinGame.setTypeOfGame("multi");
        gameMechanics.addUser(player1, joinGame);
        gameMechanics.addUser(player2, joinGame);
        gameMechanics.gmStep(0);
        @Nullable final GameSession gameSession = gameSessionService.getSessionForUser(player1);
        Assert.assertNotNull("Game session should be started on closest tick, but it didn't", gameSession);
        return gameSession;
    }
}
*/