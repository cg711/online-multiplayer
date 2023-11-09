package com.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.entity.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    public List<Game> findByActiveTrue();
    public List<Game> findByOngoingFalse();
    public List<Game> findByOngoingTrue();
}
