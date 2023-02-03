package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Likes;
import site.bookmore.bookmore.books.entity.Review;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Ranks {
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer point = 0;

    private Long ranking;

    private String nickName;

    public Ranks(Long id, Integer point) {
        this.id = id;
        this.point = point;
    }

    public static Ranks of(Long id,Integer point,Long ranking,String nickName) {
        return Ranks.builder()
                .id(id)
                .point(point)
                .ranking(ranking)
                .nickName(nickName)
                .build();
    }
}
