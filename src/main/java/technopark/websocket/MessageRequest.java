package technopark.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technopark.mechanics.requests.ClientSnap;
import technopark.mechanics.requests.JoinGame;
import technopark.mechanics.requests.FinishGame;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(JoinGame.class),
        @Type(ClientSnap.class),
        @Type(FinishGame.class)
})
public abstract class MessageRequest extends Message {
}
