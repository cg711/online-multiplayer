package com.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    
}
