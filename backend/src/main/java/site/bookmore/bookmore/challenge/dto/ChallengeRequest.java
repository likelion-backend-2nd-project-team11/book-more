package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.bookmore.bookmore.challenge.entity.Challenge;


@AllArgsConstructor
@Getter
@Builder
public class ChallengeRequest {
    private String title;
    private String description;

    // Todo. 기한 추가
    public Challenge toEntity() {
        return Challenge.builder()
                .title(this.title)
                .description(this.description)
                .build();

    }
}
