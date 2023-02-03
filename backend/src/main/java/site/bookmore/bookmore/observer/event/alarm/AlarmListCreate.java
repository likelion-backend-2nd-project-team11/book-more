package site.bookmore.bookmore.observer.event.alarm;

import lombok.Getter;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.users.entity.User;

import java.util.List;

@Getter
public class AlarmListCreate {
    private final AlarmType alarmType;
    private final List<User> targetUsers;
    private final User fromUser;
    private final Long source;

    public AlarmListCreate(AlarmType alarmType, List<User> targetUsers, User fromUser, Long source) {
        this.alarmType = alarmType;
        this.targetUsers = targetUsers;
        this.fromUser = fromUser;
        this.source = source;
    }

    public static AlarmListCreate of(AlarmType alarmType, List<User> targetUsers, User fromUser, Long source) {
        return new AlarmListCreate(alarmType, targetUsers, fromUser, source);
    }
}
