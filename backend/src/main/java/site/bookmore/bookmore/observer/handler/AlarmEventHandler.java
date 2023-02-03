package site.bookmore.bookmore.observer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.observer.event.alarm.AlarmCreate;
import site.bookmore.bookmore.observer.event.alarm.AlarmListCreate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventHandler {

    private final AlarmRepository alarmRepository;

    @Async
    @EventListener
    public void createAlarm(AlarmCreate e) {
        log.info("알람 생성 type:{}, from:{}, target:{}, source:{}", e.getAlarm().getAlarmType(), e.getAlarm().getFromUser(), e.getAlarm().getTargetUser(), e.getAlarm().getSource());
        alarmRepository.save(e.getAlarm());
        log.info("알람 생성 완료.");
    }

    @Async
    @EventListener
    public void createAlarmList(AlarmListCreate e) {
        for (int i = 0; i < e.getTargetUsers().size(); i++) {
            Alarm alarm = Alarm.builder()
                    .alarmType(AlarmType.NEW_FOLLOW_REVIEW)
                    .targetUser(e.getTargetUsers().get(i))
                    .fromUser(e.getFromUser())
                    .source(e.getSource())
                    .build();
            alarmRepository.save(alarm);
        }

        log.info("알람 생성 완료.");
    }
}
