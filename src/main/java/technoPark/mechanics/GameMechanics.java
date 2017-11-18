package technoPark.mechanics;

import org.jetbrains.annotations.NotNull;

import technoPark.mechanics.requests.JoinGame;
import technoPark.model.id.Id;
import technoPark.mechanics.requests.ClientSnap;
import technoPark.model.account.dao.AccountDao;


public interface GameMechanics {

    void addClientSnapshot(@NotNull Id<AccountDao> userId, @NotNull ClientSnap clientSnap);

    void addUser(@NotNull Id<AccountDao> user, @NotNull JoinGame joinGame);

    void gmStep(long frameTime);

    void reset();
}
