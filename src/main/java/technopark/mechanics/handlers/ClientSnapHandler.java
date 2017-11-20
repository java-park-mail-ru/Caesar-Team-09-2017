package technopark.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import technopark.mechanics.models.id.Id;
import technopark.mechanics.GameMechanics;
import technopark.mechanics.requests.ClientSnap;
import technopark.model.account.dao.AccountDao;
import technopark.websocket.MessageHandler;
import technopark.websocket.MessageHandlerContainer;

@Component
public class ClientSnapHandler extends MessageHandler<ClientSnap> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(ClientSnap.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(ClientSnap.class, this);
    }

    @Override
    public void handle(@NotNull ClientSnap message, @NotNull Id<AccountDao> forUser) {
        gameMechanics.addClientSnapshot(forUser, message);
    }
}
