package hu.game.codenames.repository;

import hu.game.codenames.model.Guess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuessRepository extends JpaRepository<Guess, Long> {
}
