package site.bookmore.bookmore.observer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.observer.event.alarm.AlarmCreate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventHandler {

    private final AlarmRepository alarmRepository;

    @Async
    @EventListener
    public void createAlarm(AlarmCreate e) {
        log.info("알람 생성 type:{}, from:{}, target:{}, source:{}", e.getAlarm().getAlarmType(), e.getAlarm().getFrom_User(), e.getAlarm().getTarget_User(), e.getAlarm().getSource());
        alarmRepository.save(e.getAlarm());
        log.info("알람 생성 완료.");
    }
}
