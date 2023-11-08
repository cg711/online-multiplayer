package com.service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.entity.Game;
import com.service.repository.GameRepository;

@Service
public class LobbySocketService {

    @Autowired
    GameRepository gameRepository;

    /**
     * TODO
     * @return
     */
    public List<Game> findAllGames() {
        try {
            return gameRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

}
