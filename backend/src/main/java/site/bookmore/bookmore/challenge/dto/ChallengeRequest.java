package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.users.entity.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChallengeRequest {
    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 50)
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 300)
    private String description;
    @NotNull(message = "기한을 입력해주세요")
    @Future
    private LocalDate deadline;
    @NotNull
    @Range(min = 0, max = 100)
    private Integer progress;

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