package technopark.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import technopark.mechanics.GameMechanics;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.requests.Upgrade;
import technopark.websocket.MessageHandler;
import technopark.websocket.MessageHandlerContainer;


@Component
public class UpgradeHandler extends MessageHandler<Upgrade> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public UpgradeHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(Upgrade.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(Upgrade.class, this);
    }

    @Override
    public void handle(@NotNull Upgrade message, @NotNull Id<AccountDao> forUser) {
        gameMechanics.tryUpdate(forUser, message);
    }
}
