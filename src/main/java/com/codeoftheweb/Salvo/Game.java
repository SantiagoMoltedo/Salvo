package com.codeoftheweb.Salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date creationDate;

    @OneToMany(mappedBy="gameId", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    public Map<String, Object> makeGameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("Id", this.getId());
        dto.put("Created", this.getCreationDate());
        dto.put("Game Players", this.getGamePlayers()
                .stream()
                .map(GamePlayer::makeGamePlayerDTO)
                .collect(Collectors.toList()));
        return dto;
    }
    @JsonIgnore
    public List<Player> getPlayerId() {
        return gamePlayers.stream().map(GamePlayer::getPlayerId).collect(Collectors.toList());
    }

    public Game () { }

    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Game(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public long getId() {
        return id;
    }
}
