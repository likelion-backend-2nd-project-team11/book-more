package site.bookmore.bookmore.alarms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.entity.AlarmType;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private boolean confirmed;
    private Long fromUserId;
    private String fromUserNickname;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> source;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDatetime;

    public static AlarmResponse of(Alarm alarm, Map<String, Object> source) {
        return AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .confirmed(alarm.isConfirmed())
                .fromUserId(alarm.getFromUser().getId())
                .fromUserNickname(alarm.getFromUser().getNickname())
                .source(source)
                .createdDatetime(alarm.getCreatedDatetime())
                .build();
    }
}
