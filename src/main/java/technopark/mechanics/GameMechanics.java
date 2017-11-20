package technopark.mechanics;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.requests.JoinGame;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.requests.ClientSnap;
import technopark.model.account.dao.AccountDao;


public interface GameMechanics {

    void addClientSnapshot(@NotNull Id<AccountDao> userId, @NotNull ClientSnap clientSnap);

    void addUser(@NotNull Id<AccountDao> user, @NotNull JoinGame joinGame);

    void gmStep(long frameTime);

    void reset();
}
