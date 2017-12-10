package technopark.mechanics.requests;

import org.jetbrains.annotations.NotNull;
import technopark.websocket.MessageRequest;

public class JoinGame extends MessageRequest {

    @NotNull
    private String typeOfGame;

    @NotNull
    public String getTypeOfGame() {
        return typeOfGame;
    }

    public void setTypeOfGame(@NotNull String typeOfGame) {
        this.typeOfGame = typeOfGame;
    }

}
