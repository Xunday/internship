package com.game;

import com.game.entity.Player;

public interface PlayerMapper {
    PlayerDto fromPlayer(Player source);

    Player toPlayer(PlayerDto target);
}
