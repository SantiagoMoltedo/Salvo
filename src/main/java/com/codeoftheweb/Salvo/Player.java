package com.codeoftheweb.Salvo;

/*Se importan librerias para utilizar*/
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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


    public Player(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
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