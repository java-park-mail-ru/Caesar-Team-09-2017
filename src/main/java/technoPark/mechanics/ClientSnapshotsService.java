package technoPark.mechanics;

import technoPark.mechanics.models.Coords;
import technoPark.mechanics.models.GameUser;
import technoPark.mechanics.models.MechanicPart;
import technoPark.mechanics.models.MousePart;
import technoPark.mechanics.multi.GameSession;
import technoPark.mechanics.requests.ClientSnap;
import technoPark.model.account.dao.AccountDao;
import technoPark.model.id.Id;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Not thread safe! Per models mechanic service
 */
@Service
public class ClientSnapshotsService {

    private final Map<Id<AccountDao>, List<ClientSnap>> snaps = new HashMap<>();

    public void pushClientSnap(@NotNull Id<AccountDao> user, @NotNull ClientSnap snap) {
        this.snaps.putIfAbsent(user, new ArrayList<>());
        final List<ClientSnap> clientSnaps = snaps.get(user);
        clientSnaps.add(snap);
    }

    @NotNull
    public List<ClientSnap> getSnapForUser(@NotNull Id<AccountDao> user) {
        return snaps.getOrDefault(user, Collections.emptyList());
    }

    public void processSnapshotsFor(@NotNull GameSession gameSession) {
        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            final List<ClientSnap> playerSnaps = getSnapForUser(player.getAccountId());
            if (playerSnaps.isEmpty()) {
                continue;
            }

            playerSnaps.stream().filter(ClientSnap::isDrill).findFirst().ifPresent(snap -> processClick(snap, gameSession, player));

            final ClientSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
            processMouseMove(player, lastSnap.getMouse());
        }
    }

    private void processClick(@NotNull ClientSnap snap, @NotNull GameSession gameSession, @NotNull GameUser gameUser) {
        final MechanicPart mechanicPart = gameUser.claimPart(MechanicPart.class);
        if (mechanicPart.tryFire()) {
//            gameSession.getBoard().drillAt(snap.getMouse());
        }
    }

    private void processMouseMove(@NotNull GameUser gameUser, @NotNull Coords mouse) {
        gameUser.claimPart(MousePart.class).setMouse(mouse);
    }

    public void clearForUser(Id<AccountDao> userProfileId) {
        snaps.remove(userProfileId);
    }

    public void reset() {
        snaps.clear();
    }
}
