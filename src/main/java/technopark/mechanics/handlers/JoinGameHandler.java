package technopark.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import technopark.mechanics.GameMechanics;
import technopark.mechanics.requests.JoinGame;
import technopark.model.account.dao.AccountDao;
import technopark.model.id.Id;
import technopark.websocket.MessageHandler;
import technopark.websocket.MessageHandlerContainer;


@Component
public class JoinGameHandler extends MessageHandler<JoinGame> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.class, this);
    }

    @Override
    public void handle(@NotNull JoinGame message, @NotNull Id<AccountDao> forUser) {
        gameMechanics.addUser(forUser, message);
    }
}
