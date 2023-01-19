package site.bookmore.bookmore.users.repositroy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.User;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerAndFollowingAndDeletedDatetimeIsNull(User follower, User following);

    Page<Follow> findByFollowerAndDeletedDatetimeIsNull(Pageable pageable, User follower);

    Page<Follow> findByFollowingAndDeletedDatetimeIsNull(Pageable pageable, User following);
}
