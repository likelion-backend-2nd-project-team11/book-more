package site.bookmore.bookmoreserver.users.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmoreserver.users.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}
