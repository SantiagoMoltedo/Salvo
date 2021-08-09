package com.codeoftheweb.Salvo;

import org.hibernate.boot.model.source.spi.IdentifierSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @RequestMapping("/api/games")
    public List<Map<String,Object>> getGameDTO() {
        return gameRepo.findAll()
                .stream()
                .map(Game::makeGameDTO)
                .collect(Collectors.toList());
    }
}
