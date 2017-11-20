package technopark.mechanics.responses;

import technopark.websocket.MessageResponse;

public class FinishGame extends MessageResponse {
    private Overcome overcome;

    public FinishGame(Overcome overcome) {
        this.overcome = overcome;
    }

    public Overcome getOvercome() {
        return overcome;
    }

    @SuppressWarnings("FieldNamingConvention")
    public enum Overcome {
        WIN,
        LOSE,
        DRAW
    }
}
