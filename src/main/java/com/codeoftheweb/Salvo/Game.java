package com.codeoftheweb.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
}
