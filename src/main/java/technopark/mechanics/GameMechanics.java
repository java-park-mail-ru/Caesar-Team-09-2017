package technopark.mechanics;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.requests.JoinGame;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.requests.ClientSnap;
import technopark.account.dao.AccountDao;
import technopark.mechanics.requests.Upgrade;


public interface GameMechanics {

    void addClientSnapshot(@NotNull Id<AccountDao> userId, @NotNull ClientSnap clientSnap);

    void addUser(@NotNull Id<AccountDao> user, @NotNull JoinGame joinGame);

    void closeShop(@NotNull Id<AccountDao> user);

    void tryUpdate(@NotNull Id<AccountDao> user, @NotNull Upgrade upgrade);

    void gmStep(long frameTime);

    void reset();

    void finishGame(@NotNull Id<AccountDao> userId);
}
