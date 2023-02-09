package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Ranks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer point = 0;

    private Long ranking;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_ranks_user"))
    private User user;

    public void updatePoint(Integer point) {
        if (point != null) this.point = point;
    }

    public void updateRanking(Long ranking) {
        if (ranking != null) this.ranking = ranking;
    }

    public void delete() {
        this.ranking = -1L;
    }

    public static Ranks of(Integer point, Long ranking, User user) {
        return Ranks.builder()
                .point(point)
                .ranking(ranking)
                .user(user)
                .build();
    }
}
