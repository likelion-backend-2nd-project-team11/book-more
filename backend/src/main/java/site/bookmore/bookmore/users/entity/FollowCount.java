package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FollowCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer followerCount;

    private Integer followingCount;

    public void plusFollowerCount(Integer followerCount) {
        this.followerCount = followerCount + 1;
    }

    public void minusFollowerCount(Integer followerCount) {
        this.followerCount = followerCount - 1;
    }

    public void plusFollowingCount(Integer followingCount) {
        this.followingCount = followingCount + 1;
    }

    public void minusFollowingCount(Integer followingCount) {
        this.followingCount = followingCount - 1;
    }
}
