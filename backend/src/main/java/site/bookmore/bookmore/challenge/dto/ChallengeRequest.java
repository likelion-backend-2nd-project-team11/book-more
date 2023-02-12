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
    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(max = 50, message = "제목은 50자 이하로 작성해 주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(max = 300, message = "본문은 300자 이하로 작성해 주세요.")
    private String description;
    @NotNull(message = "기한을 입력해 주세요.")
    @Future(message = "기한을 현재 날짜 이후로 설정해 주세요.")
    private LocalDate deadline;
    @NotNull(message = "진행도를 입력해 주세요.")
    @Range(min = 0, max = 100, message = "진행도는 0부터 100사이의 정수만 입력해 주세요.")
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