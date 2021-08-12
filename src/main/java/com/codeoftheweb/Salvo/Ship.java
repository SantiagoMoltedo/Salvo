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

    private String shipType;

    @ElementCollection
    @Column(name="shipLocations")
    private List<String> location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Map<String, Object> makeShipDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getShipType());
        dto.put("locations", this.getLocation());
        return dto;
    }


    public Ship() {    }

    public Ship(String shipType, GamePlayer gamePlayer, List<String> location) {
        this.shipType = shipType;
        this.gamePlayer = gamePlayer;
        this.location = location;
    }

    //SHIPTYPE
    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    //GAMEPLAYERID get set
    public GamePlayer getGamePlayerId() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    //Location
    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }
}
