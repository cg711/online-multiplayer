package com.service.controller;

import java.io.IOException;
import java.util.List;

import com.service.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.service.services.LobbySocketService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Websocket endpoints for Lobby.
 */
@Slf4j
@Controller
public class LobbySocketController {

    private static LobbySocketService lobbySocketService;

    private static SimpMessageSendingOperations messageTemplate;

    @Autowired
    public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
        messageTemplate = simpMessageSendingOperations;
    }

    @Autowired
    public void setLobbySocketService(LobbySocketService service) {
        lobbySocketService = service;
    }

    @MessageMapping("/join/{gameId}")
    public void attemptJoinGame(@DestinationVariable("gameId") Long gameId, SimpMessageHeaderAccessor accessor) {
        boolean didJoin = lobbySocketService.attemptJoinGame(gameId, accessor);
        messageTemplate.convertAndSend(WebsocketConstants.LOBBY + lobbySocketService.getUserIdFromHeader(accessor), Boolean.toString(didJoin));
    }


}
