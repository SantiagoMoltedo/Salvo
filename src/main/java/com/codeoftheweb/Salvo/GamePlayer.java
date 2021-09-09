package com.codeoftheweb.Salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

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

    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }

    public Map<String, Object> MakeHitDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("self", this.getSelfHits());
        dto.put("opponent", this.getOpponentHits());
        return dto;
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

