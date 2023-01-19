package site.bookmore.bookmore.users.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.users.entity.Follow;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(Long follower, Long following);
}
