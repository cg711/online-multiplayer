package com.service.controller;

import com.service.DTO.RedirectDTO;
import com.service.entity.Account;
import com.service.services.LobbySocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

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
        // LOBBY HANDLER
        if (destination.matches(WebsocketConstants.LOBBY_REGEX)) {
            // If user doesn't already exist in game
            Long gameId = lobbySocketService.findUserInGame(headerAccessor);
            if(gameId == null) {
                messageTemplate.convertAndSend(WebsocketConstants.LOBBY + lobbySocketService.getSubscriptionId(destination), lobbySocketService.findAllGames());
            } else {
                // else, send redirection url to
                messageTemplate.convertAndSend(
                        WebsocketConstants.LOBBY + lobbySocketService.getSubscriptionId(destination),
                        RedirectDTO.builder().redirectURL(WebsocketConstants.GAME_REDIRECT_URL + gameId).build());
            }
        } else if (destination.matches(WebsocketConstants.GAME_REGEX)) {
            messageTemplate.convertAndSend(WebsocketConstants.GAME + lobbySocketService.getSubscriptionId(destination), "Hello");
        }
    }

    /**
     * Handles all websocket disconnects.
     * @param event
     */
    @EventListener
    public void handleWebsocketClose(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("User " + headerAccessor.getUser().getName() + " disconnected.");
        String destination = headerAccessor.getDestination();
        if(destination.matches(WebsocketConstants.GAME_REGEX)) {
            lobbySocketService.removeUserFromGame(headerAccessor);
        }
    }
}
