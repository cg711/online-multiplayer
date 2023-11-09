package com.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class GameSocketController {

    private static SimpMessageSendingOperations messageTemplate;

    @Autowired
    public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
        messageTemplate = simpMessageSendingOperations;
    }

    @MessageMapping("/game/{gameId}")
    public void handleGame(@DestinationVariable Long gameId) {
        log.info("" + gameId);
    }
}
