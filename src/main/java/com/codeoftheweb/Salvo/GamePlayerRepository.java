package com.codeoftheweb.Salvo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long>{
    List<GamePlayer> findByPlayerId(Player PlayerId);

    List<GamePlayer> findByGameId(Game GameId);

    List<GamePlayer> findByJoinDate(Date JoinDate);
}
