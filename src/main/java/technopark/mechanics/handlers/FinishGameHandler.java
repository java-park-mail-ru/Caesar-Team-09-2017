package technopark.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import technopark.mechanics.models.id.Id;
import technopark.mechanics.GameMechanics;
import technopark.mechanics.requests.ClientSnap;
import technopark.mechanics.requests.FinishGame;
import technopark.model.account.dao.AccountDao;
import technopark.websocket.MessageHandler;
import technopark.websocket.MessageHandlerContainer;

@Component
public class FinishGameHandler extends MessageHandler<FinishGame> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public FinishGameHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(FinishGame.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(FinishGame.class, this);
    }

    @Override
    public void handle(@NotNull FinishGame message, @NotNull Id<AccountDao> forUser) {
        gameMechanics.finishGame(message.getUserId());
    }
}

