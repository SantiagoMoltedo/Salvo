package com.codeoftheweb.Salvo;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

//    @RequestMapping(path="/game/{gameid}/players", method = {RequestMethod.GET, RequestMethod.PUT})
//    public  ResponseEntity<Map> joinGame (Authentication authentication) {
//        if (gamePlayerRepository.)
//
//    }

    @RequestMapping(value = "/game/{gameid}/players",method = {RequestMethod.GET,RequestMethod.POST})
    public  ResponseEntity<Map> joinGameButton(@PathVariable Long gameid,Authentication authentication) {

        if(playerRepository.findByuserName(authentication.getName()) == null){
            return new ResponseEntity<>(makeMap("No estas logeado pa",0), HttpStatus.UNAUTHORIZED);
        }
        if(gameRepository.findAll().stream().anyMatch(x -> x.getId()==gameid)){
            return new ResponseEntity<>(makeMap("Nou such Gam3",0), HttpStatus.FORBIDDEN);
        }
        if(gameRepository.getById(gameid).getGamePlayers().size() >=2){
            if (gameRepository.getById(gameid).getGamePlayers().stream().findFirst().get().getPlayer().getId() != playerRepository.findByUserName(authentication.getName()).getId()){
                return new ResponseEntity<>(makeMap("error","No te unas a tu propio juego master"), HttpStatus.FORBIDDEN);
            }
            System. out. println("CANTIDAD:"+ gameRepository.getById(gameid).getGamePlayers().size());
            return new ResponseEntity<>(makeMap("no hay lugar",0), HttpStatus.FORBIDDEN);

        }
        Date newDate = new Date ();
        GamePlayer newGamePlayer = gamePlayerRepository.save(new
                GamePlayer(newDate,
                gameRepository.getById(gameid),
                this.playerRepository.findByuserName(authentication.getName()
                )));
        return new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()), HttpStatus.CREATED);

    }

    @PostMapping(path="/games")
    public  ResponseEntity<Map<String, Object>> findGamePlayer(Authentication authentication) {

        Date Tiempo = new Date();
        Game newGame = gameRepository.save(new Game(Tiempo));
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(new Date(),newGame, this.playerRepository.findByuserName(authentication.getName())));

        return new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/games")
    public Map<String,Object> Game (Authentication authentication) {
        Map<String, Object> map = new LinkedHashMap<String, Object> ();

        if(isGuest(authentication)){
            map.put("player","Guest");
        }else{
            map.put("player",playerRepository.findByUserName(authentication.getName()).makePlayerDTO());

        }
        map.put ("games",  gameRepository.findAll()
                .stream()
                .map(Game::makeGameDTO)
                .collect(Collectors.toList()));
        return map;
    }

    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method=RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> requestShips(@PathVariable long gamePlayerId) {
        if (gamePlayerRepository.findById(gamePlayerId).isPresent()) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("ship", gamePlayerRepository.findById(gamePlayerId).get().getShips().stream().map(b -> b.makeShipDTO()).collect(Collectors.toList()));

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(makeMap("Este gameplayer no esciste", 0), HttpStatus.FORBIDDEN);
    }


    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(@PathVariable long gamePlayerId,
                                                        @RequestBody List<Ship> ships,
                                                        Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","No estas logueado padre"),
                                        HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == null){
            return new ResponseEntity<>(makeMap("error","No existe el gp ingresado"),
                                        HttpStatus.UNAUTHORIZED);
        }

        if (playerRepository.findByUserName(authentication.getName()).getGamePlayers().stream().noneMatch(x -> x.getId() == gamePlayerId) ){
            return new ResponseEntity<>(makeMap("error","No estas en el juego que queres modificar"),
                                        HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getShips().size() != 0){
            return new ResponseEntity<>(makeMap("error","Ya tenes ships en este gamePlayer"),
                                        HttpStatus.FORBIDDEN);
        }

//      PARTE DE LA LISTA
        for (Ship ship: ships){
            if(!ship.getType().equals("carrier") & ship.getType().equals("battleship") &
                ship.getType().equals("submarine") & ship.getType().equals("destroyer") &
                ship.getType().equals("patrolboat")){

                return new ResponseEntity<>(makeMap("error", "Nombres de los barcos incorrectos"),
                                            HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("carrier") & ship.getShipLocations().size() != 5){
                return new ResponseEntity<>(makeMap("error","Los barcos no tienen valores validos"),
                                            HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("battleship") & ship.getShipLocations().size() != 4){
                return new ResponseEntity<>(makeMap("error","Los barcos no tienen valores validos"),
                                            HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("submarine") & ship.getShipLocations().size() != 3){
                return new ResponseEntity<>(makeMap("error","Los barcos no tienen valores validos"),
                                            HttpStatus.UNAUTHORIZED);
            }
            if (ship.getType().equals("destroyer") & ship.getShipLocations().size() != 3){
                return new ResponseEntity<>(makeMap("error","Los barcos no tienen valores validos"),
                                            HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("patrolboat") & ship.getShipLocations().size() != 2){
                return new ResponseEntity<>(makeMap("error","Los barcos no tienen valores validos"),
                                            HttpStatus.FORBIDDEN);
            }
        }
        for (Ship ship: ships){
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        }
        return new ResponseEntity<>(makeMap("ship", gamePlayerRepository.findById(gamePlayerId).get().getShips().stream().map(b -> b.makeShipDTO()).collect(Collectors.toList())),HttpStatus.CREATED);
    }

    public Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayer.getGame().getId());

        dto.put("created", gamePlayer.getGame().getCreationDate());

        dto.put("gameState", "PLACESHIPS");

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

        dto.put("hits", gamePlayer.MakeHitDTO());
        return dto;
    }

    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String,Object>> findGamePlayerId(@PathVariable Long nn, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).get();
        if(playerRepository.findByuserName(authentication.getName()).getGamePlayers()
                                                                    .stream()
                                                                    .anyMatch(b->b.getId()==nn) ) {

            return new ResponseEntity<>(makeGameViewDTO(gamePlayer), HttpStatus.ACCEPTED);
        }else
        {
            return new ResponseEntity<>(makeMap("No hagas trampas chistosito", true),HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String email,@RequestParam String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByuserName(email);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }
        Player nPlayer = playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("id", nPlayer.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap();
        map.put(key,value);
        return map;
    }


    

}
