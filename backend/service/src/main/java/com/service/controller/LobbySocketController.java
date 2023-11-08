package com.service.controller;

import java.io.IOException;
import java.util.List;

import com.service.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.service.services.LobbySocketService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * Websocket endpoints for Lobby.
 */
@Slf4j
@Controller
public class LobbySocketController {

    private static LobbySocketService lobbySocketService;

    @Autowired
    public void setLobbySocketService(LobbySocketService service) {
        lobbySocketService = service;
    }

    /**
     * Responsible for sending all avalaible game information.
     */
    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public List<Game> getGames(StompHeaderAccessor accessor) {
        for(String s : accessor.getSessionAttributes().keySet()) {
            log.info(s);
        }
        return null;
    }
}
