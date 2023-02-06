package site.bookmore.bookmore.reviews.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer professionalism;
    private Integer fun;
    private Integer readability;
    private Integer collectible;
    private Integer difficulty;

    // 도서 리뷰 수정
    public void update(Chart chart) {
        updateProfessionalism(chart.getProfessionalism());
        updateFun(chart.getFun());
        updateReadability(chart.getReadability());
        updateCollectible(chart.getCollectible());
        updateDifficulty(chart.getDifficulty());
    }

    private void updateProfessionalism(Integer professionalism) {
        if (professionalism != null) {
            this.professionalism = professionalism;
        }
    }

    private void updateFun(Integer fun) {
        if (fun != null) {
            this.fun = fun;
        }
    }

    private void updateReadability(Integer readability) {
        if (readability != null) {
            this.readability = readability;
        }
    }

    private void updateCollectible(Integer collectible) {
        if (collectible != null) {
            this.collectible = collectible;
        }
    }

    private void updateDifficulty(Integer difficulty) {
        if (difficulty != null) {
            this.difficulty = difficulty;
        }
    }
}