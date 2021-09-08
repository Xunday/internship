package com.game;

import com.game.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapperImpl implements PlayerMapper {
    @Override
    public PlayerDto fromPlayer(Player source) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setBanned(source.getBanned());
        playerDto.setBirthday(source.getBirthday());
        playerDto.setExperience(source.getExperience());
        playerDto.setName(source.getName());
        playerDto.setTitle(source.getTitle());
        playerDto.setProfession(source.getProfession());
        playerDto.setRace(source.getRace());

        return playerDto;
    }

    @Override
    public Player toPlayer(PlayerDto target) {
        return null;
    }
}
