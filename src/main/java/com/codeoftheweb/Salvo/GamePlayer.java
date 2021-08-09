package com.codeoftheweb.Salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game gameId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player playerId;

    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("Id", this.getId());
        dto.put("Player", this.getPlayerId().makePlayerDTO());
        return dto;
    }

    public GamePlayer () { }

    public GamePlayer(Date joinDate, Game gameId, Player playerId) {
        this.joinDate = joinDate;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

    public Player getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Player playerId) {
        this.playerId = playerId;
    }

    public long getId() {
        return id;
    }
}
