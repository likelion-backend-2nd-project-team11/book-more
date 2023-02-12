package site.bookmore.bookmore.alarms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.users.entity.User;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByTargetUserAndDeletedDatetimeIsNull(User target, Pageable pageable);

    Page<Alarm> findByTargetUserAndConfirmedIsFalseAndDeletedDatetimeIsNull(User target, Pageable pageable);

    List<Alarm> findByFromUser(User fromUser);
}
