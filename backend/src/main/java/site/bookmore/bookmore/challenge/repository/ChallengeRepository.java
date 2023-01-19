package site.bookmore.bookmore.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.challenge.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {

}
