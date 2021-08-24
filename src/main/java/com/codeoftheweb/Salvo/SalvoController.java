package com.codeoftheweb.Salvo;

import org.hibernate.boot.model.source.spi.IdentifierSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping("/games")
    public Map<String,Object> Game () {
        Map<String, Object> map = new LinkedHashMap<String, Object> ();
        map.put ("games",  gameRepo.findAll()
                .stream()
                .map(Game::makeGameDTO)
                .collect(Collectors.toList()));
        return map;
    }

    public Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayer.getGame().getId());

        dto.put("created", gamePlayer.getGame().getCreationDate());

        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(GamePlayer::makeGamePlayerDTO)
                .collect(Collectors.toList()));

        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(Ship::makeShipDTO)
                .collect(Collectors.toList()));

        dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(GamePlayer->GamePlayer.getSalvos() //flatMap sirve para aplanar una lista que tiene otra lista adentro (la lista mas grande) para unirlas
                        .stream()
                        .map(Salvo::makeSalvoDTO))
                        .collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String,Object> findGamePlayerId(@PathVariable Long nn) {
        GamePlayer gamePlayer = gamePlayerRepo.findById(nn).get();
        return makeGameViewDTO(gamePlayer);
    }

}
