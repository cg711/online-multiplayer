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

    /**
     * Returns the user id of a destination, which in most cases is the trailing path variable
     * @param dest
     * @return
     */
    public String getSubscriptionId(String dest) {
        String[] arr = dest.split("/");
        return arr[arr.length - 1];
    }

}
