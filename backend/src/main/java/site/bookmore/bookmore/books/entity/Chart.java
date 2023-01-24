package site.bookmore.bookmore.books.entity;

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
    private int professionalism;
    private int fun;
    private int readability;
    private int collectible;
    private int difficulty;

    // 도서 리뷰 수정
    public void update(Chart chart) {
        updateProfessionalism(chart.getProfessionalism());
        updateFun(chart.getFun());
        updateReadability(chart.getReadability());
        updateCollectible(chart.getCollectible());
        updateDifficulty(chart.getDifficulty());
    }

    private void updateProfessionalism(int professionalism) {
        this.professionalism = professionalism;
    }

    private void updateFun(int fun) {
        this.fun = fun;
    }

    private void updateReadability(int readability) {
        this.readability = readability;
    }

    private void updateCollectible(int collectible) {
        this.collectible = collectible;
    }

    private void updateDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}