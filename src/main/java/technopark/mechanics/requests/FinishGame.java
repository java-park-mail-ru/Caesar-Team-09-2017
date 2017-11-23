package technopark.mechanics.requests;

import technopark.websocket.MessageRequest;

public class FinishGame {
    public static final class Request extends MessageRequest {
        private long userId;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }
}
