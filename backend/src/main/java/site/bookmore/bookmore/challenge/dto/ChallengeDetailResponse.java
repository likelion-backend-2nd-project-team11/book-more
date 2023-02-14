package site.bookmore.bookmore.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import site.bookmore.bookmore.challenge.entity.Challenge;

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


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDatetime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime LastModifiedDatetime;


    public static Page<ChallengeDetailResponse> toDtoList(Page<Challenge> postEntities) {
        Page<ChallengeDetailResponse> postDetailResponses = postEntities.map(m -> ChallengeDetailResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .description(m.getDescription())
                .progress(m.getProgress())
                .completed(m.isCompleted())
                .deadline(m.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .createdDatetime(m.getCreatedDatetime())
                .LastModifiedDatetime(m.getLastModifiedDatetime())
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
                .createdDatetime(challenge.getCreatedDatetime())
                .LastModifiedDatetime(challenge.getLastModifiedDatetime())
                .build();
    }
}
