package site.bookmore.bookmore.alarms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.users.dto.UserDetailResponse;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private UserDetailResponse fromUser;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> source;
    private String createdDatetime;

    public static AlarmResponse of(Alarm alarm, Map<String, Object> source) {
        return AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUser(UserDetailResponse.of(alarm.getFromUser()))
                .source(source)
                .createdDatetime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(alarm.getCreatedDatetime()))
                .build();
    }
}
