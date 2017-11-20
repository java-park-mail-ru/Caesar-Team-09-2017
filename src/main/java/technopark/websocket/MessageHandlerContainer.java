package technopark.websocket;

import org.jetbrains.annotations.NotNull;

import technopark.model.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;

public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull Id<AccountDao> forUser) throws HandleException;

    <T extends MessageRequest> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
