package technopark.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import technopark.services.AccountService;
import technopark.account.dao.AccountDao;
import technopark.mechanics.models.id.Id;

import java.io.IOException;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");

    @NotNull
    private AccountService accountService;
    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;
    @NotNull
    private final RemotePointService remotePointService;

    private final ObjectMapper objectMapper;


    public GameSocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                             @NotNull AccountService authService,
                             @NotNull RemotePointService remotePointService,
                             ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.accountService = authService;
        this.remotePointService = remotePointService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final String username = (String) webSocketSession.getAttributes().get("username");
        LOGGER.info("register");
        if (username == null || accountService.getAccountFromUsername(username) == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in. Openning websocket session is denied.");
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        final Id<AccountDao> userId = Id.of(accountService.getAccountFromUsername(username).getIdLong());
        remotePointService.registerUser(userId, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final String username = (String) webSocketSession.getAttributes().get("username");
        final AccountDao accountDao;
        LOGGER.info("message from client");
        if (username == null || (accountDao = accountService.getAccountFromUsername(username)) == null) {
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        handleMessage(accountDao, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(AccountDao accountDao, TextMessage text) {
        final MessageRequest messageRequest;
        try {
            messageRequest = objectMapper.readValue(text.getPayload(), MessageRequest.class);
            LOGGER.info(text.getPayload());
        } catch (IOException ex) {
            LOGGER.error("wrong json format at mechanics.models response", ex);
            return;
        }
        try {
            //noinspection ConstantConditions
            messageHandlerContainer.handle(messageRequest, accountDao.getId());
        } catch (HandleException e) {
            LOGGER.error("Can't handle messageRequest of type " + messageRequest.getClass().getName() + " with content: " + text, e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final String username = (String) webSocketSession.getAttributes().get("userId");
        if (username == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        LOGGER.info("disconnect client");
        remotePointService.removeUser(Id.of(accountService.getAccountFromUsername(username).getIdLong()));
    }

    private void closeSessionSilently(@NotNull WebSocketSession session, @Nullable CloseStatus closeStatus) {
        final CloseStatus status;
        if (closeStatus == null) {
            status = SERVER_ERROR;
        } else {
            status = closeStatus;
        }

        LOGGER.info("disconnect client silently");
        //noinspection OverlyBroadCatchBlock
        try {
            session.close(status);
        } catch (Exception ignore) {
            ignore.toString();
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
