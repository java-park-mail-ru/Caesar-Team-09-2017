package technoPark.mechanics.responses;

import org.jetbrains.annotations.NotNull;
import technoPark.mechanics.models.GameUser;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;
import technoPark.websocket.MessageResponse;

import java.util.Map;

public class InitGameMultiPlayer {
    public static final class Response extends MessageResponse {
        private Id<AccountDao> self;
        private Id<AccountDao> enemy;
        private Map<Id<AccountDao>, GameUser.ServerPlayerSnap> players;
        private Map<Id<AccountDao>, String> names;
        private Map<Id<AccountDao>, String> colors;

        @NotNull
        public Map<Id<AccountDao>, String> getNames() {
            return names;
        }

        public void setNames(@NotNull Map<Id<AccountDao>, String> names) {
            this.names = names;
        }

        @NotNull
        public Id<AccountDao> getSelf() {
            return self;
        }

        public Id<AccountDao> getEnemy() {
            return enemy;
        }

        public void setEnemy(Id<AccountDao> enemy) {
            this.enemy = enemy;
        }

        @NotNull
        public Map<Id<AccountDao>, String> getColors() {
            return colors;
        }

        public void setColors(@NotNull Map<Id<AccountDao>, String> colors) {
            this.colors = colors;
        }

        public void setSelf(@NotNull Id<AccountDao> self) {
            this.self = self;
        }
        @NotNull
        public Map<Id<AccountDao>, GameUser.ServerPlayerSnap> getPlayers() {
            return players;
        }

        public void setPlayers(@NotNull Map<Id<AccountDao>, GameUser.ServerPlayerSnap> players) {
            this.players = players;
        }
    }
}
