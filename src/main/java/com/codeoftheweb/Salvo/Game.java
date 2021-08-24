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

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> Score;

    public Map<String, Object> makeGameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getGamePlayers()
                .stream()
                .map(GamePlayer::makeGamePlayerDTO)
                .collect(Collectors.toList()));
        dto.put("scores", this.getGamePlayers() //El if en el medio para que haya o no score, se muestre en el JSON
                .stream()
                .map(gamePlayer ->  {
                    if(gamePlayer.getScore().isPresent()){     //Is present hace referencia a que no es nulo
                        return gamePlayer.getScore().get().makeScoreDTO();
                    }
                    else{
                        return null;
                    }
                }));
        return dto;
    }
    @JsonIgnore
    public List<Player> getPlayerId() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(Collectors.toList());
    }

    public Game () { }


    public Set<com.codeoftheweb.Salvo.Score> getScore() {
        return Score;
    }

    public void setScore(Set<com.codeoftheweb.Salvo.Score> score) {
        Score = score;
    }

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
