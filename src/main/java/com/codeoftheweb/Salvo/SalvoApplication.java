package com.codeoftheweb.Salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean //Con Bean defino jugadores hard codeados
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {
            //Guardado de PLAYERS
			Player player1 = new Player("Jugador hard codeado 1"); //Creo al Player 1, que es hard codeado y va a estar siempre en el repositorio
            playerRepository.save(player1); //Guardo en el repositorio al jugador hard codeado 1

			Player player2 = new Player("Jugador hard codeado 2"); //Lo mismo que player 1
            playerRepository.save(player2); //Guardo en el repositorio al jugador hard codeado 2

			Player player3 = new Player("Jugador hard codeado 3");
			playerRepository.save(player3);

			Player player4 = new Player("Jugador hard codeado 4");
			playerRepository.save(player4);

            //Guardado de GAMES (2 juegos)
			Date creationDate1 = new Date();
			Game game1 = new Game(creationDate1);
			gameRepository.save(game1);

            Date creationDate2 = Date.from(creationDate1.toInstant().plusSeconds(3600));
			Game game2 = new Game(creationDate2);
			gameRepository.save(game2);

			//Guardado de GAMEPLAYERS (4 jugadores de juegos 2 x game)
			Date joinDate1 = new Date ();
			GamePlayer gamePlayer1 = new GamePlayer(joinDate1,game1,player1);
            gamePlayerRepository.save(gamePlayer1);

			Date joinDate2 = new Date ();
			GamePlayer gamePlayer2 = new GamePlayer(joinDate2,game1,player2);
			gamePlayerRepository.save(gamePlayer2);

            Date joinDate3 = new Date ();
            GamePlayer gamePlayer3 = new GamePlayer(joinDate3,game2,player3);
            gamePlayerRepository.save(gamePlayer3);

            Date joinDate4 = new Date ();
            GamePlayer gamePlayer4 = new GamePlayer(joinDate4,game2,player4);
            gamePlayerRepository.save(gamePlayer4);

            //Guardado de SHIPS (4 ships solo para game 1 x ahora)
            List<String> location1 = Arrays.asList("A1","B1","C1");
            Ship ship1 = new Ship("Destroyer",gamePlayer1,location1); //Ship de gp1
            shipRepository.save(ship1);

            List<String> location2 = Arrays.asList("E4","E5","E6","E7","E8");
            Ship ship2 = new Ship("Patrol Boat",gamePlayer1,location2); //Ship de gp1
            shipRepository.save(ship2);

            List<String> location3 = Arrays.asList("B7","C7","D7");
            Ship ship3 = new Ship("Destroyer",gamePlayer2,location3); //Ship de gp2
            shipRepository.save(ship3);

            List<String> location4 = Arrays.asList("J3","J4","J5","J6","J7");
            Ship ship4 = new Ship("Carrier",gamePlayer2,location4); //Ship de gp2
            shipRepository.save(ship4);

            //Guardado de SALVOS
            Salvo salvo1 = new Salvo(1,Arrays.asList("J3"),gamePlayer1);
			salvoRepository.save(salvo1);

			Salvo salvo2 = new Salvo(1,Arrays.asList("A1","B1","C1","D1"),gamePlayer2);
			salvoRepository.save(salvo2);

			Salvo salvo3 = new Salvo(2,Arrays.asList("J4","J5","J6","J7","J8"),gamePlayer1);
			salvoRepository.save(salvo3);

			Salvo salvo4 = new Salvo(2,Arrays.asList("C3"),gamePlayer2);
			salvoRepository.save(salvo4);

			Salvo salvo5 = new Salvo(3,Arrays.asList("H2"),gamePlayer1);
			salvoRepository.save(salvo5);

			Salvo salvo6 = new Salvo(3,Arrays.asList("E4","E5","E6","E7","E8"),gamePlayer2);
			salvoRepository.save(salvo6);

			};
	}
}