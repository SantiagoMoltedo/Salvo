package com.codeoftheweb.Salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean //Con Bean defino jugadores hard codeados
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {

			Player player1 = new Player("Jugador hard codeado 1"); //Creo al Player 1, que es hard codeado y va a estar siempre en el repositorio
            playerRepository.save(player1); //Guardo en el repositorio al jugador hard codeado 1

			Player player2 = new Player("Jugador hard codeado 2"); //Lo mismo que player 1
            playerRepository.save(player2); //Guardo en el repositorio al jugador hard codeado 2

			Player player3 = new Player("Jugador hard codeado 3");
			playerRepository.save(player3);

			Player player4 = new Player("Jugador hard codeado 4");
			playerRepository.save(player4);


			Date creationDate1 = new Date();
			Game game1 = new Game(creationDate1);
			gameRepository.save(game1);

            Date creationDate2 = Date.from(creationDate1.toInstant().plusSeconds(3600));
			Game game2 = new Game(creationDate2);
			gameRepository.save(game2);

			Date creationDate3 = Date.from(creationDate2.toInstant().plusSeconds(3600));
			Game game3 = new Game(creationDate3);
			gameRepository.save(game3);

			Date creationDate4 = Date.from(creationDate3.toInstant().plusSeconds(3600));
			Game game4 = new Game(creationDate4);
			gameRepository.save(game4);

			Date creationDate5 = Date.from(creationDate4.toInstant().plusSeconds(3600));
			Game game5 = new Game(creationDate5);
			gameRepository.save(game5);

			Date creationDate6 = Date.from(creationDate5.toInstant().plusSeconds(3600));
			Game game6 = new Game(creationDate6);
			gameRepository.save(game6);


			Date joinDate1 = new Date ();
			GamePlayer gamePlayer1 = new GamePlayer(joinDate1,game1,player1);
            gamePlayerRepository.save(gamePlayer1);

			Date joinDate2 = new Date ();
			GamePlayer gamePlayer2 = new GamePlayer(joinDate2,game1,player2);
			gamePlayerRepository.save(gamePlayer2);
			};
	}
}