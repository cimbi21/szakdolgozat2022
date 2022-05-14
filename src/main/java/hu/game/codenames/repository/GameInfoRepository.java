package hu.game.codenames.repository;

import hu.game.codenames.model.GameInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameInfoRepository extends JpaRepository<GameInfo, Long> {

}
