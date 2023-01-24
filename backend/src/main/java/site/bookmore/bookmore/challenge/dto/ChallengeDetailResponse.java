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

    private String nickname;

    private String title;

    private String description;

    private int progress;

    private boolean completed;

    private String deadline;


    private String createdDatetime;

    private String LastModifiedDatetime;


    public static Page<ChallengeDetailResponse> toDtoList(Page<Challenge> postEntities) {
        Page<ChallengeDetailResponse> postDetailResponses = postEntities.map(m -> ChallengeDetailResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .description(m.getDescription())
                .progress(m.getProgress())
                .completed(m.isCompleted())
                .deadline(m.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .createdDatetime(m.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .LastModifiedDatetime(m.getLastModifiedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .build());
        return postDetailResponses;
    }

    public static ChallengeDetailResponse of(Challenge challenge){
        return ChallengeDetailResponse.builder()
                .id(challenge.getId())
                .nickname(challenge.getOwner().getNickname())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .progress(challenge.getProgress())
                .completed(challenge.isCompleted())
                .deadline(challenge.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .createdDatetime(challenge.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .LastModifiedDatetime(challenge.getLastModifiedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .build();
    }
}
