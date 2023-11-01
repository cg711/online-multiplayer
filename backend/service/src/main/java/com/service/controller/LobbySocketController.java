package com.service.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.service.services.LobbySocketService;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

/**
 * Websocket endpoints for Lobby.
 */
@ServerEndpoint("/lobby")
@Slf4j
@Controller
public class LobbySocketController {

    private static LobbySocketService lobbySocketService;

    @Autowired
    public void setLobbySocketService(LobbySocketService service) {
        lobbySocketService = service;
    }
    
    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        log.info("Session " + session.getId() + " opened.");
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        session.getBasicRemote().sendText(objectWriter.writeValueAsString(lobbySocketService.findAllGames()));
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @OnClose
    public void onClose() {
        log.info("Session closed.");
    }
}
