package site.bookmore.bookmore.alarms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.alarms.entity.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public Page<AlarmResponse> findByFollowingReview(Pageable pageable, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Long targetId = user.getId();

        // 해당 유저id의 alarm만 조회
        return alarmRepository.findByTarget_User(targetId, pageable).map(alarm -> new AlarmResponse(alarm));
    }
}
