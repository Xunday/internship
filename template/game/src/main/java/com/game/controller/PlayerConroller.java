package com.game.controller;

import com.game.PlayerDto;
import com.game.PlayerMapperImpl;
import com.game.PlayerSpecification;
import com.game.SearchCriteria;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest/players",
        produces = {MediaType.APPLICATION_JSON_VALUE})
@ControllerAdvice
public class PlayerConroller {

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerMapperImpl playerMapper;

    @PostMapping
    public Player create(
            @Validated @RequestBody Player player
    ) {
        try {
            if (!(isCorrBirthDate(player.getBirthday())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data, please, check input data (date)");
            if (!(isCorrExperience(player.getExperience())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data, please, check input data (experience)");

            player.setLevel(calcLevel(player.getExperience()));
            player.setUntilNextLevel(calcUntilNextLevel(player.getLevel(), player.getExperience()));

            if (player.getBanned() == null)
                player.setBanned(false);

            return playerService.createPlayer(player);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public static Boolean isCorrExperience(Integer exp) {
        return exp >= 0 && exp <= 10000000;
    }

    public static Boolean isCorrBirthDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) >= 2000 && calendar.get(Calendar.YEAR) <= 3000;
    }

    public static Integer calcLevel(Integer exp) {
        return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }

    public static Integer calcUntilNextLevel(Integer level, Integer exp) {
        return (int) Math.round(50 * (level + 1) * (level + 2) - exp);
    }

    @GetMapping("/{id}")
    public Player getPlayerByID(@PathVariable Long id) {
        if (id == null || id == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return playerService.getPlayerByID(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such player by player id"));
    }

    @PostMapping("/{id}")
    public Player updatePlayerByID(@PathVariable Long id,
                                   @RequestBody(required = false) PlayerDto playerDto) {

        if (id == null || id == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (playerDto.getBirthday() != null)
            if (!(isCorrBirthDate(playerDto.getBirthday())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data, please, check input data (experience,date)");
        if (playerDto.getExperience() != null)
            if (!(isCorrExperience(playerDto.getExperience())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data, please, check input data (experience)");

        return playerService.getPlayerByID(id)
                .map(player -> {
                    if (playerDto.getName() != null)
                        player.setName(playerDto.getName());
                    if (playerDto.getTitle() != null)
                        player.setTitle(playerDto.getTitle());
                    if (playerDto.getProfession() != null)
                        player.setProfession(playerDto.getProfession());
                    if (playerDto.getRace() != null)
                        player.setRace(playerDto.getRace());
                    if (playerDto.getBirthday() != null)
                        player.setBirthday(playerDto.getBirthday());
                    if (playerDto.getBanned() != null)
                        player.setBanned(playerDto.getBanned());
                    if (playerDto.getExperience() != null) {
                        player.setExperience(playerDto.getExperience());
                        player.setLevel(calcLevel(playerDto.getExperience()));
                        player.setUntilNextLevel(calcUntilNextLevel(player.getLevel(), playerDto.getExperience()));
                    }
                    return player;
                })
                .map(player -> playerService.updatePlayer(player))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such player by player id"));

    }

    @DeleteMapping("/{id}")
    public void deletePlayerByID(@PathVariable Long id) {
        if (id == null || id == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        playerService.getPlayerByID(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such player by player id"));
        playerService.deletePlayer(id);
    }

    @GetMapping("/count")
    public Long getCount(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "profession", required = false) Profession profession,
                         @RequestParam(value = "race", required = false) Race race,
                         @RequestParam(value = "after", required = false) Long after,
                         @RequestParam(value = "before", required = false) Long before,
                         @RequestParam(value = "banned", required = false) Boolean banned,
                         @RequestParam(value = "minExperience", required = false) Integer minExperience,
                         @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                         @RequestParam(value = "minLevel", required = false) Integer minLevel,
                         @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        PlayerSpecification specName = null;
        PlayerSpecification specTitle = null;
        PlayerSpecification specProf = null;
        PlayerSpecification specRace = null;
        PlayerSpecification specAfter = null;
        PlayerSpecification specBefore = null;
        PlayerSpecification specMinExp = null;
        PlayerSpecification specMaxExp = null;
        PlayerSpecification specMinL = null;
        PlayerSpecification specMaxL = null;
        PlayerSpecification specBanned = null;
        if (name != null)
            specName = new PlayerSpecification(new SearchCriteria("name", ":", name));
        if (title != null)
            specTitle = new PlayerSpecification(new SearchCriteria("title", ":", title));
        if (profession != null)
            specProf = new PlayerSpecification(new SearchCriteria("profession", ":", profession));
        if (race != null)
            specRace = new PlayerSpecification(new SearchCriteria("race", ":", race));
        if (after != null)
            specAfter = new PlayerSpecification(new SearchCriteria("birthday", ">", after));
        if (before != null)
            specBefore = new PlayerSpecification(new SearchCriteria("birthday", "<", before));
        if (minExperience != null)
            specMinExp = new PlayerSpecification(new SearchCriteria("experience", ">", minExperience));
        if (maxExperience != null)
            specMaxExp = new PlayerSpecification(new SearchCriteria("experience", "<", maxExperience));
        if (minLevel != null)
            specMinL = new PlayerSpecification(new SearchCriteria("level", ">", minLevel));
        if (maxLevel != null)
            specMaxL = new PlayerSpecification(new SearchCriteria("level", "<", maxLevel));
        if (banned != null)
            specBanned = new PlayerSpecification(new SearchCriteria("banned", ":", banned));

        return playerService.count(
                Specification.where(specName)
                        .and(specTitle)
                        .and(specAfter)
                        .and(specBefore)
                        .and(specProf)
                        .and(specRace)
                        .and(specMinExp)
                        .and(specMaxExp)
                        .and(specMinL)
                        .and(specMaxL)
                        .and(specBanned));
    }

    @GetMapping
    public List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize
    ) {
        PlayerSpecification specName = null;
        PlayerSpecification specTitle = null;
        PlayerSpecification specProf = null;
        PlayerSpecification specRace = null;
        PlayerSpecification specAfter = null;
        PlayerSpecification specBefore = null;
        PlayerSpecification specMinExp = null;
        PlayerSpecification specMaxExp = null;
        PlayerSpecification specMinL = null;
        PlayerSpecification specMaxL = null;
        PlayerSpecification specBanned = null;
        if (name != null)
            specName = new PlayerSpecification(new SearchCriteria("name", ":", name));
        if (title != null)
            specTitle = new PlayerSpecification(new SearchCriteria("title", ":", title));
        if (profession != null)
            specProf = new PlayerSpecification(new SearchCriteria("profession", ":", profession));
        if (race != null)
            specRace = new PlayerSpecification(new SearchCriteria("race", ":", race));
        if (after != null)
            specAfter = new PlayerSpecification(new SearchCriteria("birthday", ">", after));
        if (before != null)
            specBefore = new PlayerSpecification(new SearchCriteria("birthday", "<", before));
        if (minExperience != null)
            specMinExp = new PlayerSpecification(new SearchCriteria("experience", ">", minExperience));
        if (maxExperience != null)
            specMaxExp = new PlayerSpecification(new SearchCriteria("experience", "<", maxExperience));
        if (minLevel != null)
            specMinL = new PlayerSpecification(new SearchCriteria("level", ">", minLevel));
        if (maxLevel != null)
            specMaxL = new PlayerSpecification(new SearchCriteria("level", "<", maxLevel));
        if (banned != null)
            specBanned = new PlayerSpecification(new SearchCriteria("banned", ":", banned));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, order.getFieldName());

        return playerService.getAllPlayers(
                Specification.where(specName)
                        .and(specTitle)
                        .and(specAfter)
                        .and(specBefore)
                        .and(specProf)
                        .and(specRace)
                        .and(specMinExp)
                        .and(specMaxExp)
                        .and(specMinL)
                        .and(specMaxL)
                        .and(specBanned), pageable).stream().collect(Collectors.toList());
    }

}
