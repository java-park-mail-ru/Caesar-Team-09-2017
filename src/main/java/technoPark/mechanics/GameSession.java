package technoPark.mechanics;

import org.jetbrains.annotations.NotNull;
import technoPark.mechanics.models.GameUser;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;

public abstract class GameSession {
    public abstract void terminateSession();

    public abstract boolean isFinished();

    @NotNull
    public abstract GameUser getPlayer();

    @NotNull
    public abstract GameUser getFirst();

    @NotNull
    public abstract GameUser getSecond();

    public abstract GameUser getEnemy(@NotNull Id<AccountDao> userId);
}