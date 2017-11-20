package technopark.mechanics.models.player;

import org.jetbrains.annotations.NotNull;

import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.Snap;
import technopark.mechanics.models.part.GamePart;
import technopark.mechanics.models.part.MechanicPart;
import technopark.mechanics.models.part.MousePart;
import technopark.model.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;

import java.util.HashMap;
import java.util.Map;

public class GameUser extends GameObject {
    @NotNull
    private final AccountDao accountDao;

    public GameUser(@NotNull AccountDao accountDao, @NotNull MechanicsTimeService timeService) {
        this.accountDao = accountDao;
        addPart(MousePart.class, new MousePart());
        addPart(MechanicPart.class, new MechanicPart(timeService));
    }

    @NotNull
    public AccountDao getAccountDao() {
        return accountDao;
    }

    @NotNull
    public Id<AccountDao> getAccountId() {
        return accountDao.getId();
    }

    @Override
    public @NotNull ServerPlayerSnap getSnap() {
        return ServerPlayerSnap.snapPlayer(this);
    }

    public static class ServerPlayerSnap implements Snap<GameUser> {
        private Id<AccountDao> userId;

        Map<String, Snap<? extends GamePart>> gameParts;

        public Id<AccountDao> getUserId() {
            return userId;
        }

        public Map<String, Snap<? extends GamePart>> getGameParts() {
            return gameParts;
        }

        public void setUserId(Id<AccountDao> userId) {
            this.userId = userId;
        }

        @NotNull
        public static ServerPlayerSnap snapPlayer(@NotNull GameUser gameUser) {
            final ServerPlayerSnap serverPlayerSnap = new ServerPlayerSnap();
            serverPlayerSnap.userId = gameUser.getAccountDao().getId();
            serverPlayerSnap.gameParts = new HashMap<>();
            gameUser.getPartSnaps().forEach(part -> serverPlayerSnap.gameParts.put(part.getClass().getSimpleName(), part));
            return serverPlayerSnap;
        }
    }

    @Override
    public String toString() {
        return "GameUser{"
                + "accountDao=" + accountDao
                + '}';
    }
}
