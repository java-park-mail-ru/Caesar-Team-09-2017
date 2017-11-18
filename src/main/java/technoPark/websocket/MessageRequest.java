package technoPark.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technoPark.mechanics.requests.ClientSnap;
import technoPark.mechanics.requests.JoinGame;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(JoinGame.class),
        @Type(ClientSnap.class)
        })
public abstract class MessageRequest extends Message {
}
