package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.Tier;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private Long followerId;
    private String nickname;
    private Integer followerCount;
    private Integer followingCount;
    private Tier tier;
    private String createdDatetime;

    public FollowerResponse(Follow follow) {
        this.id = follow.getId();
        this.followerId = follow.getFollower().getId();
        this.nickname = follow.getFollower().getNickname();
        this.followerCount = follow.getFollower().getFollowCount().getFollowerCount();
        this.followingCount = follow.getFollower().getFollowCount().getFollowingCount();
        this.tier = follow.getFollower().getTier();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getCreatedDatetime());
    }
}
