package site.bookmore.bookmore.challenge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.users.entity.User;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    Page<Challenge> findByOwner(Pageable pageable, User owner);

}
