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
    private Long id;

    @Column(nullable = false)
    private Integer point = 0;

    private Long ranking;

    public Ranks(Long id, Integer point) {
        this.id = id;
        this.point = point;
    }

}