package technopark.websocket;

import org.jetbrains.annotations.NotNull;

import technopark.model.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;

public abstract class MessageHandler<T extends MessageRequest> {
    @NotNull
    private final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(@NotNull Message message, @NotNull Id<AccountDao> forUser) throws HandleException {
        try {
            handle(clazz.cast(message), forUser);
        } catch (ClassCastException ex) {
            throw new HandleException("Can't read incoming messageRequest of type " + message.getClass(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Id<AccountDao> forUser) throws HandleException;
}
