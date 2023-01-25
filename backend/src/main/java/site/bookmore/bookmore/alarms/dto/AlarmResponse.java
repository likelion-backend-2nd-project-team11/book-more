package site.bookmore.bookmore.alarms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.entity.AlarmType;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private Long targetUser;
    private Long fromUser;
    private Long source;
    private String createdDatetime;


    public AlarmResponse(Alarm alarm) {
        this.id = alarm.getId();
        this.alarmType = alarm.getAlarmType();
        this.targetUser = alarm.getTargetUser().getId();
        this.fromUser = alarm.getFromUser().getId();
        this.source = alarm.getSource();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(alarm.getCreatedDatetime());
    }


}