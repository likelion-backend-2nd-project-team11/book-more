package site.bookmore.bookmore.users.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.users.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndDeletedDatetimeIsNull(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
