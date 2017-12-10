package technopark.websocket;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import technopark.mechanics.requests.FinishGame;
import technopark.mechanics.requests.JoinGame;


@SuppressWarnings("OverlyBroadThrowsClause")
public class MessageSerializationTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void joinGameRequest() throws IOException {
        final JoinGame request = new JoinGame();
        request.setTypeOfGame("single");
        final String requestJson = objectMapper.writeValueAsString(request);
        final MessageRequest fromJson = objectMapper.readValue(requestJson, MessageRequest.class);
        Assert.assertTrue(fromJson instanceof JoinGame);
    }

    @Test
    public void FinishGameRequest() throws IOException {
        final FinishGame.Request request = new FinishGame.Request();
        final String requestJson = objectMapper.writeValueAsString(request);
        final MessageRequest fromJson = objectMapper.readValue(requestJson, MessageRequest.class);
        Assert.assertTrue(fromJson instanceof FinishGame.Request);
    }
}
