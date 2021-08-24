package com.codeoftheweb.Salvo;

/*Se importan librerias para utilizar*/
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity /*Se cre la clase como una entidad o objeto*/
public class Player {

    @Id /*Se le asigna una ID (player 1, o 2 o x)*/
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") /*Que se genere automaticmente la ID*/
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName; //Defino el string de UserName para el nombre del usuaria (su mail)

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;
    
    public Map<String, Object> makePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("email", this.getUserName());
        dto.put("id", this.getId());
        return dto;
    }

    @JsonIgnore
    public List<Game> getGameId() {
        return gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toList());
    }

    public Player() {
    } //Construyo Player

    public Player(String user) { //Construyo player con un argumento que es un string user que va a ser el User Name
        this.userName = user;
    }

    public String getUserName() { //Recivo UserName
        return userName;
    }

    public void setUserName(String UserName) { //Seteo UserName a un nuevo player
        this.userName = UserName;
    }

    public String toString() {
        return userName;
    }


    public Player(Set<GamePlayer> gamePlayers, Set<Score> scores) {
        this.gamePlayers = gamePlayers;
        this.scores = scores;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    //Para obtener los scores de un player XQ ESTOY EN PLAYER de un solo Game, xq si jugue a varios juegos
    //Se van a mandar todos los scores de todos los juegos del player.
    public Optional<Score> getScore(Game game) {
        return this.scores.stream().filter(s -> s.getGame()/*.getId()*/ == game/*.getId()*/).findFirst(); //findFirst agarra el primero,  y el filter filtra para agarrar el game que coincida (1 solo)
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