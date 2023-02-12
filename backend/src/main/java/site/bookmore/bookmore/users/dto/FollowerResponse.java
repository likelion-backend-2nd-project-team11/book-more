package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.Follow;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private Long followerId;
    private String nickname;
    private String profile;
    private Integer followerCount;
    private Integer followingCount;

    public FollowerResponse(Follow follow) {
        this.id = follow.getId();
        this.followerId = follow.getFollower().getId();
        this.nickname = follow.getFollower().getNickname();
        this.profile = follow.getFollower().getProfile();
        this.followerCount = follow.getFollower().getFollowCount().getFollowerCount();
        this.followingCount = follow.getFollower().getFollowCount().getFollowingCount();
    }
}
