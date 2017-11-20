package technopark.mechanics.requests;

import technopark.websocket.MessageRequest;

public class JoinGame extends MessageRequest {

    private String typeOfGame;

    public String getTypeOfGame() {
        return typeOfGame;
    }

    public void setTypeOfGame(String typeOfGame) {
        this.typeOfGame = typeOfGame;
    }

}
