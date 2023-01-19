package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.users.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Builder
@Getter
public class ChallengeDetailResponse {
    private Long id;

    private User owner;

    private String title;

    private String description;

    private int progress;

    private boolean completed;

    private LocalDate deadline;


    private LocalDateTime createdDateTime;

    private String LastModifiedDatetime;

    private String deletedDatetime;

    public static Page<ChallengeDetailResponse> toDtoList(Page<Challenge> postEntities) {
        Page<ChallengeDetailResponse> postDetailResponses = postEntities.map(m -> ChallengeDetailResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .description(m.getDescription())
                .progress(m.getProgress())
                .completed(m.isCompleted())
                .deadline(m.getDeadline())
                .createdDateTime(m.getCreatedDatetime())
                .LastModifiedDatetime(m.getLastModifiedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .deletedDatetime(m.getDeletedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .build());
        return postDetailResponses;
    }

}
