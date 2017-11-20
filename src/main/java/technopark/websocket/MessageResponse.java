package technopark.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technopark.mechanics.responses.FinishGame;
import technopark.mechanics.responses.InitGameMultiPlayer;
import technopark.mechanics.responses.InitGameSinglePlayer;
import technopark.mechanics.responses.ServerSnap;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(ServerSnap.class),
        @Type(InitGameSinglePlayer.Response.class),
        @Type(InitGameMultiPlayer.Response.class),
        @Type(FinishGame.class)
})
public abstract class MessageResponse extends Message {
}
