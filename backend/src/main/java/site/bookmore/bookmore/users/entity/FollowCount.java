package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FollowCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    private Integer followerCount;

    @PositiveOrZero
    private Integer followingCount;

    public void plusFollowerCount() {
        this.followerCount++;
    }

    public void minusFollowerCount() {
        this.followerCount--;
    }

    public void plusFollowingCount() {
        this.followingCount++;
    }

    public void minusFollowingCount() {
        this.followingCount--;
    }
}
