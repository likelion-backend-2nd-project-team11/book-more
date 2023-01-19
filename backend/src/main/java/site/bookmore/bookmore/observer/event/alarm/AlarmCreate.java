package site.bookmore.bookmore.observer.event.alarm;

import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.users.entity.User;

public class AlarmCreate {

    private final Alarm alarm;


    public AlarmCreate(Alarm alarm) {
        this.alarm = alarm;
    }

    /**
     * [service 등록 기능에 추가]
     * [] -> 변경해야 할 매개변수들
     * private final ApplicationEventPublisher publisher;
     * publisher.publishEvent(AlarmEvent.of(AlarmType.[], [User targetId]알림 받는 사람, [User fromId]알림 발생자, [Long sourceId]));
     * // sourceId는 리뷰id, 좋아요id, 팔로우id (pk)들을 나타낸다.
     */
    public static AlarmCreate of(AlarmType alarmType, User targetId, User fromId, Long sourceId) {
        return new AlarmCreate(Alarm.builder()
                .alarmType(alarmType)
                .targetUser(targetId)
                .fromUser(fromId)
                .source(sourceId)
                .build());
    }

    public Alarm getAlarm() {
        return alarm;
    }

}
