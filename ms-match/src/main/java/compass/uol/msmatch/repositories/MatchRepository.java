package compass.uol.msmatch.repositories;

import compass.uol.msmatch.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>{
}
