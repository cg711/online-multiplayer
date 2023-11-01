package com.service.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.service.entity.Game;
import com.service.repository.AccountRepository;
import com.service.repository.GameRepository;

@Service
public class TestPopulate implements ApplicationRunner  {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Game game = Game.builder()
        // .active(true)
        // .dateCreated(new Date(Calendar.getInstance().getTime().getTime()))
        // .maxPlayers(4)
        // .players(new ArrayList<>()).build();
        // game.getPlayers().add(accountRepository.findByUsername("cg711").get(0));

        // gameRepository.save(game);
    }
}
