package site.bookmore.bookmore.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.reviews.entity.Chart;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChartRequest {
    @Min(value = 1, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    @Max(value = 5, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    private Integer professionalism;
    @Min(value = 1, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    @Max(value = 5, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    private Integer fun;
    @Min(value = 1, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    @Max(value = 5, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    private Integer readability;
    @Min(value = 1, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    @Max(value = 5, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    private Integer collectible;
    @Min(value = 1, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    @Max(value = 5, message = "1 ~ 5점 사이의 점수만 부여할 수 있습니다.")
    private Integer difficulty;

    public Chart toEntity() {
        return Chart.builder()
                .professionalism(professionalism)
                .fun(fun)
                .readability(readability)
                .collectible(collectible)
                .difficulty(difficulty)
                .build();
    }
}
