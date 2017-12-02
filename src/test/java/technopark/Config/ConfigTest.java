package technopark.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import technopark.account.dao.AccountDao;
import technopark.mechanics.Config;
import technopark.mechanics.MechanicsTimeService;
import technopark.mechanics.models.id.Id;
import technopark.mechanics.models.player.GameUser;
import technopark.mechanics.requests.ClientSnap;
import technopark.mechanics.responses.InitGameSinglePlayer;

import java.io.IOException;

import static technopark.mechanics.Config.*;

public class ConfigTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void comparatorBonusTest() throws IOException {
        boolean flag = true;
        for (int i = 1; i < COUNT_OF_BONUSES; i++) {
            boolean yCondition = BONUS_POSITION[i - 1].y > BONUS_POSITION[i].y;
            boolean xCondition = BONUS_POSITION[i - 1].y == BONUS_POSITION[i].y && BONUS_POSITION[i - 1].x > BONUS_POSITION[i].x;
            if (yCondition || xCondition) {
                flag = false;
                break;
            }
            Assert.assertTrue(flag);
        }
    }
}
