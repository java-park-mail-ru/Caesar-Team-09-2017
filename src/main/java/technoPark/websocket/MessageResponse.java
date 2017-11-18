package technoPark.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technoPark.mechanics.responses.FinishGame;
import technoPark.mechanics.responses.InitGameMultiPlayer;
import technoPark.mechanics.responses.InitGameSinglePlayer;
import technoPark.mechanics.responses.ServerSnap;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(ServerSnap.class),
        @Type(InitGameSinglePlayer.Response.class),
        @Type(InitGameMultiPlayer.Response.class),
        @Type(FinishGame.class)
})
public abstract class MessageResponse extends Message {
}
