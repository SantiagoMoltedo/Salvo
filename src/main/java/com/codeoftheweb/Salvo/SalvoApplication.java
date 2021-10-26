package com.codeoftheweb.Salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	@Bean //Con Bean defino jugadores hard codeados
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {
			//Guardado de PLAYERS  CAMBIAR PLAYERS Y PONER SUS CONTRASEÑAS DEL PDF Y USAS encode().
			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24")); //Creo al Player 1, que es hard codeado y va a estar siempre en el repositorio
			playerRepository.save(player1); //Guardo en el repositorio al jugador hard codeado 1

			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42")); //Lo mismo que player 1
			playerRepository.save(player2); //Guardo en el repositorio al jugador hard codeado 2

			Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			playerRepository.save(player3);

			Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
			playerRepository.save(player4);

			//Guardado de GAMES (4 juegos)
			Date creationDate1 = new Date();
			Game game1 = new Game(creationDate1);
			gameRepository.save(game1);

			Date creationDate2 = Date.from(creationDate1.toInstant().plusSeconds(3600));
			Game game2 = new Game(creationDate2);
			gameRepository.save(game2);

			Date creationDate3 = Date.from(creationDate2.toInstant().plusSeconds(3600));
			Game game3 = new Game(creationDate3);
			gameRepository.save(game3);

			//Guardado de GAMEPLAYERS (6 jugadores de juegos 2 x game)
			Date joinDate1 = new Date();
			GamePlayer gamePlayer1 = new GamePlayer(joinDate1, game1, player1);
			gamePlayerRepository.save(gamePlayer1);

			Date joinDate2 = new Date();
			GamePlayer gamePlayer2 = new GamePlayer(joinDate2, game1, player2);
			gamePlayerRepository.save(gamePlayer2);

			Date joinDate3 = new Date();
			GamePlayer gamePlayer3 = new GamePlayer(joinDate3, game2, player3);
			gamePlayerRepository.save(gamePlayer3);

			Date joinDate4 = new Date();
			GamePlayer gamePlayer4 = new GamePlayer(joinDate4, game2, player4);
			gamePlayerRepository.save(gamePlayer4);

			Date joinDate5 = new Date();
			GamePlayer gamePlayer5 = new GamePlayer(joinDate5, game3, player1);
			gamePlayerRepository.save(gamePlayer5);

			Date joinDate6 = new Date();
			GamePlayer gamePlayer6 = new GamePlayer(joinDate6, game3, player4);
			gamePlayerRepository.save(gamePlayer6);
		};
	}
}



@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepository;
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName -> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("PLAYER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
//				.antMatchers("/admin/**").hasAuthority("ADMIN")
//				.antMatchers("/**").hasAuthority("PLAYER")
				.antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
				.antMatchers("/api/login").permitAll()
                .antMatchers("/web/**").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/**").hasAuthority("PLAYER")
				.and().headers().frameOptions().disable()
				.and().csrf().ignoringAntMatchers("/h2-console/")
				.and()
				.cors().disable()

				;
//                .and()
//                .formLogin()

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}