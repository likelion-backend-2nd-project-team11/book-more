package site.bookmore.bookmore.users.repositroy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerAndFollowingAndDeletedDatetimeIsNull(User follower, User following);

    Page<Follow> findByFollowerAndDeletedDatetimeIsNull(Pageable pageable, User follower);

    Page<Follow> findByFollowingAndDeletedDatetimeIsNull(Pageable pageable, User following);

    List<Follow> findAllByFollowingAndDeletedDatetimeIsNull(User following);
    @Query("select f from Follow f " +
            "join fetch f.follower follower " +
            "join fetch f.following following " +
            "join fetch follower.followCount " +
            "join fetch  following.followCount " +
            "where follower.id = :userId or following.id = :userId")
    List<Follow> findAllFollowOfUser(Long userId);
}
