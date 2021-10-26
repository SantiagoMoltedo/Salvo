package com.codeoftheweb.Salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date joinDate;

    @ElementCollection
    @Column(name="opponentHits")
    private List<String> opponentHits;

    @ElementCollection
    @Column(name="selfHits")
    private List<String> selfHits;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvos;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private int carrier;
    private int battleship;
    private int submarine;
    private int destroyer;
    private int patrolboat;


    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }

    public Map<String, Object> makeHitsDTO(GamePlayer gamePlayer){
        Map<String, Object>     dto = new LinkedHashMap<>();
        if(gamePlayer.getGame().getGamePlayers().size() == 2) {
            GamePlayer oponente = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> gp != gamePlayer).findFirst().get();
            if(oponente.getShips().size() != 0 && gamePlayer.getShips().size() != 0) {
                dto.put("self", oponente.getSalvos().stream().map(salvo -> makeHitsSelfOpponentDTO(oponente, salvo)));
                dto.put("opponent", this.getSalvos().stream().map(salvo -> makeHitsSelfOpponentDTO(gamePlayer, salvo)));
                return dto;
            }
        }
        dto.put("self", new ArrayList<>());
        dto.put("opponent", new ArrayList<>());
        return dto;
    }

    public Map<String, Object> makeHitsSelfOpponentDTO(GamePlayer gamePlayer, Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", salvo.getTurnNumber());
        dto.put("hitLocations", this.getHitLocations(gamePlayer, salvo));
        dto.put("damages", this.getDamages(this.getHitLocations(gamePlayer, salvo), gamePlayer, salvo));
        dto.put("missed", salvo.getSalvoLocations().size() - this.getHitLocations(gamePlayer, salvo).size());
        return dto;
    }

    public List<String> getHitLocations(GamePlayer gamePlayer, Salvo salvo){
        List<String> hits = new ArrayList<>();

        GamePlayer oponente = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> gp != gamePlayer).findFirst().get();
        List<List<String>> shipLocations = oponente
                .getShips()
                .stream()
                .map(Ship::getShipLocations)
                .collect(Collectors.toList());
        for(String salvoLocation : salvo.getSalvoLocations()){
            for(List<String> listaDeShipLocations : shipLocations){
                for(String location: listaDeShipLocations){
                    if(salvoLocation.equals(location)){
                        hits.add(salvoLocation);
                    }
                }
            }
        }
        return hits;
    }

    public Map<String, Object> getDamages(List<String> hits, GamePlayer gamePlayer, Salvo salvo){
        int carrierHits = 0;
        int battleshipHits = 0;
        int submarineHits = 0;
        int destroyerHits = 0;
        int patrolboatHits = 0;
        int carrier = 0;
        int battleship = 0;
        int submarine = 0;
        int destroyer = 0;
        int patrolboat = 0;

        GamePlayer oponente = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> gp != gamePlayer).findFirst().get();
        Set<Ship> setDeShip = oponente.getShips();
        for(Ship ship : setDeShip){
            for(String hit : hits) {
                if (ship.getShipLocations().contains(hit)) {
                    if (ship.getType().equals("destroyer")) {
                        destroyerHits++;
                    }
                    if (ship.getType().equals("patrolboat")) {
                        patrolboatHits++;
                    }
                    if (ship.getType().equals("carrier")) {
                        carrierHits++;
                    }
                    if (ship.getType().equals("battleship")) {
                        battleshipHits++;
                    }
                    if (ship.getType().equals("submarine")) {
                        submarineHits++;
                    }
                }
            }
        }
        List<List<Salvo>> allHits = new ArrayList<>();
        allHits.add(gamePlayer.getSalvos().stream().filter(sv -> sv.getTurnNumber() <= salvo.getTurnNumber()).collect(Collectors.toList()));
        for(Ship ship : setDeShip) {
            for (List<Salvo> allHitsList : allHits) {
                for (Salvo hit : allHitsList) {
                    for(String hitLocation : hit.getSalvoLocations()){
                        if(ship.getShipLocations().contains(hitLocation)){
                            if (ship.getType().equals("destroyer")) {
                                destroyer++;
                            }
                            if (ship.getType().equals("patrolboat")) {
                                patrolboat++;
                            }
                            if (ship.getType().equals("carrier")) {
                                carrier++;
                            }
                            if (ship.getType().equals("battleship")) {
                                battleship++;
                            }
                            if (ship.getType().equals("submarine")) {
                                submarine++;
                            }
                        }
                    }
                }
            }
        }
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("carrierHits", carrierHits);
        dto.put("battleshipHits", battleshipHits);
        dto.put("submarineHits", submarineHits);
        dto.put("destroyerHits", destroyerHits);
        dto.put("patrolboatHits", patrolboatHits);
        dto.put("carrier", carrier);
        dto.put("battleship", battleship);
        dto.put("submarine", submarine);
        dto.put("destroyer", destroyer);
        dto.put("patrolboat", patrolboat);
        return dto;
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap();
        map.put(key,value);
        return map;
    }


    public GamePlayer() {  }

    public GamePlayer(Date joinDate, Game game, Player player) {
        this.joinDate = joinDate;
        this.game = game;
        this.player = player;
    }

    public GamePlayer(List<String> opponentHits, List<String> selfHits) {
        this.opponentHits = opponentHits;
        this.selfHits = selfHits;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }


    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    public Optional<Score> getScore () {
        return this.player.getScore(game);
    }

    public List<String> getOpponentHits() {
        return opponentHits;
    }

    public void setOpponentHits(List<String> opponentHits) {
        this.opponentHits = opponentHits;
    }

    public List<String> getSelfHits() {
        return selfHits;
    }

    public void setSelfHits(List<String> selfHits) {
        this.selfHits = selfHits;
    }

    public long getId() {
        return id;
    }

}

