package technoPark.websocket;

import org.jetbrains.annotations.NotNull;

import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;

public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull Id<AccountDao> forUser) throws HandleException;

    <T extends MessageRequest> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
