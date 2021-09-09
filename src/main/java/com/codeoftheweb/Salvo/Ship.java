package com.codeoftheweb.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String type;

    @ElementCollection
    @Column(name="shipLocations")
    private List<String> shipLocations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Map<String, Object> makeShipDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getType());
        dto.put("locations", this.getShipLocations());
        return dto;
    }


    public Ship() {    }

    public Ship(String type, GamePlayer gamePlayer, List<String> shipLocations) {
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.shipLocations = shipLocations;
    }

    //SHIPTYPE
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //GAMEPLAYERID get set
    public GamePlayer getGamePlayerId() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    //Location
    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }
}
