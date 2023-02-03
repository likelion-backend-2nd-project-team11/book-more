package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.users.entity.User;

import javax.validation.constraints.*;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChallengeRequest {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    private String description;
    @NotNull(message = "기한을 입력해주세요")
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    @NotNull
    @Range(min = 0, max = 100)
    private Integer progress;


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