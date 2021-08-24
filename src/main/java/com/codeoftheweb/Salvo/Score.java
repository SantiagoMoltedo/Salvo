package com.codeoftheweb.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player")
    private Player player;

    private Double score;

    private Date finnishDate;

    public Map<String, Object> makeScoreDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", this.getPlayer().getId());
        dto.put("score", this.score);
        dto.put("finnishDate", this.finnishDate);
        return dto;
    }


    public Score() {  }

    public Score(Game game, Player player, Double score, Date finnishDate) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.finnishDate = finnishDate;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getFinnishDate() {
        return finnishDate;
    }

    public void setFinnishDate(Date finnishDate) {
        this.finnishDate = finnishDate;
    }

    public long getId() {
        return id;
    }
}
