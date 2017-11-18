package technoPark.mechanics.requests;

import technoPark.websocket.MessageRequest;

public class JoinGame extends MessageRequest{

    private String typeOfGame;

    public String getTypeOfGame() {
        return typeOfGame;
    }

    public void setTypeOfGame(String typeOfGame) {
        this.typeOfGame = typeOfGame;
    }

}
