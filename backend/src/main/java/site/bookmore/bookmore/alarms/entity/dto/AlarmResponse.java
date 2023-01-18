package site.bookmore.bookmore.alarms.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.alarms.entity.Alarm;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AlarmResponse {

    private Long id;
    private String alarmType;
    private Long targetUser;
    private Long fromUser;
    private Long source;
    private String createdDatetime;


    public AlarmResponse(Alarm alarm) {
        this.id = alarm.getId();
        this.alarmType = alarm.getAlarmType();
        this.targetUser = alarm.getTargetUser();
        this.fromUser = alarm.getFromUser();
        this.source = alarm.getSource();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(alarm.getCreatedDatetime());
    }


}
