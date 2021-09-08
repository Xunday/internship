package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public long count(Specification<Player> specification) {
        return playerRepository.findAll(specification).stream().count();
    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public Optional<Player> getPlayerByID(Long playerID) {
        return playerRepository.findById(playerID);
    }

    public Page<Player> getAllPlayers(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }


}
