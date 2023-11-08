package com.service.controller;

import com.service.services.LobbySocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Controller
@Slf4j
public class SocketEventListeners {

    private static SimpMessageSendingOperations messageTemplate;

    @Autowired
    public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
        messageTemplate = simpMessageSendingOperations;
    }

    private static LobbySocketService lobbySocketService;


    @Autowired
    public void setLobbySocketService(LobbySocketService service) {
        lobbySocketService = service;
    }

    /**
     * Handles all websocket subscriptions.
     * @param event
     */
    @EventListener
    public void handleWebsocketSubscription(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        log.info("User " + headerAccessor.getUser().getName() + " subscribed to " + destination);
        if (destination.matches(WebsocketConstants.LOBBY_REGEX)) {
            messageTemplate.convertAndSend(WebsocketConstants.LOBBY + lobbySocketService.getSubscriptionId(destination), lobbySocketService.findAllGames());
        }
    }
}
