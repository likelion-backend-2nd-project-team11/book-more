package site.bookmore.bookmore.alarms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.users.entity.User;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByTargetUser(User target, Pageable pageable);
}
