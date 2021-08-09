package com.codeoftheweb.Salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;
    private PlayerRepository playerRepo;

    @RequestMapping("/api/games")
    public List<Game> getAllGames() {
        return gameRepo.findAll();
    }

    @RequestMapping("/api/players")
    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

}
