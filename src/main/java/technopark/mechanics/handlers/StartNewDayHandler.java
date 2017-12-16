package technopark.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import technopark.mechanics.GameMechanics;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.requests.StartNewDay;
import technopark.websocket.MessageHandler;
import technopark.websocket.MessageHandlerContainer;


@Component
public class StartNewDayHandler extends MessageHandler<StartNewDay> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public StartNewDayHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(StartNewDay.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(StartNewDay.class, this);
    }

    @Override
    public void handle(@NotNull StartNewDay message, @NotNull Id<AccountDao> forUser) {
        gameMechanics.closeShop(forUser);
    }
}
