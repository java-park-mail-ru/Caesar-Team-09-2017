package technopark.mechanics.requests;

import technopark.mechanics.models.id.Id;
import technopark.model.account.dao.AccountDao;
import technopark.websocket.MessageRequest;

public class FinishGame extends MessageRequest {
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
