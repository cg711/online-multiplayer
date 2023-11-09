package com.service.services;

import java.util.List;

import com.service.entity.Account;
import com.service.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.service.entity.Game;
import com.service.repository.GameRepository;

@Service
@Slf4j
public class LobbySocketService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AccountRepository accountRepository;

    /**
     * Returns a list of active games. Active means game is either ongoing or not and not abandoned or ended.
     * @return
     */
    public List<Game> findAllGames() {
        try {
            return gameRepository.findByActiveTrue();
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

    /**
     * Dictates whether a user is currently in a non-ongoing game.
     * @param account
     * @param gameId
     * @return
     */
    public boolean doesUserExistInGame(Account account, Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        return game.getPlayers().contains(account);
    }

    /**
     * Attempts to join a user to a game.
     * @param gameId
     * @param accessor
     * @return false on failed join, true on succesful join
     */
    public boolean attemptJoinGame(Long gameId, SimpMessageHeaderAccessor accessor) {
        // retrieve account information
        Account account = accountRepository.findByUsername(accessor.getUser().getName()).get(0);
        //if game exists
        Game game = gameRepository.findById(gameId).orElseThrow();
        // if game exists, is active, isn't ongoing, and doesn't have max players
        if(game != null && game.getActive() && !game.getOngoing() && game.getPlayers().size() != game.getMaxPlayers() && !doesUserExistInGame(account, gameId)) {
            // If here, game is joinable.
            game.getPlayers().add(account);
            gameRepository.save(game);
            log.info("User " + account.getUsername() + " joined game " + gameId);
            return true;
        } else {
            // This is where spectate logic would go. Could be implemented as an enum value, "JOINED", "SPECTATOR" etc.
            log.info("Game " + gameId + " either doesn't exist, is full, is ongoing, or player already exists in game.");
            return false;
        }
    }

    public Long getUserIdFromHeader(SimpMessageHeaderAccessor accessor) {
        return accountRepository.findByUsername(accessor.getUser().getName()).get(0).getId();
    }

    public Long findUserInGame(SimpMessageHeaderAccessor accessor) {
        Account account = accountRepository.findByUsername(accessor.getUser().getName()).get(0);
        for(Game g : gameRepository.findByActiveTrue()) {
            log.info(g.getId().toString());
            for(Account a : g.getPlayers()) {
                if(a.getId().equals(account.getId())) {
                    return g.getId();
                }
            }
        }
        return null;
    }

    /**
     * Removes a user from a game. Done on websocket disconnect.
     * @param accessor
     */
    public void removeUserFromGame(SimpMessageHeaderAccessor accessor) {
        Account account = accountRepository.findByUsername(accessor.getUser().getName()).get(0);
        Game game = gameRepository.findById(findUserInGame(accessor)).orElseThrow();
        for(int i = 0; i < game.getPlayers().size(); i++) {
            if(game.getPlayers().get(i).getUsername().equals(account.getUsername())) {
                game.getPlayers().remove(i);
                break;
            }
        }
        gameRepository.save(game);
    }

}
