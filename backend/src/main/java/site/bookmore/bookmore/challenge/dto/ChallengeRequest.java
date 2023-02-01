package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.users.entity.User;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChallengeRequest {
    private String title;
    private String description;
    private LocalDate deadline;
    private int progress;


    // Todo. 기한 추가
    public Challenge toEntity(User owner) {
        return Challenge.builder()
                .owner(owner)
                .title(this.title)
                .description(this.description)
                .deadline(this.deadline)
                .progress(this.progress)
                .build();
    }

    public Challenge toEntity() {
        return Challenge.builder()
                .title(this.title)
                .description(this.description)
                .deadline(this.deadline)
                .progress(this.progress)
                .build();
    }
}