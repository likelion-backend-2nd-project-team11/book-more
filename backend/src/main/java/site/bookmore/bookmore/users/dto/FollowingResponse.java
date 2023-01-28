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
public class FollowingResponse {
    private Long id;
    private Long followingId;
    private String nickname;
    private Integer followerCount;
    private Integer followingCount;
    private Integer reviewsCount;
    private Tier tier;
    private String createdDatetime;

    public FollowingResponse(Follow follow) {
        this.id = follow.getId();
        this.followingId = follow.getFollowing().getId();
        this.nickname = follow.getFollowing().getNickname();
        this.followerCount = follow.getFollowing().getFollowerCount();
        this.followingCount = follow.getFollowing().getFollowingCount();
        this.reviewsCount = follow.getFollowing().getReviewCount();
        this.tier = follow.getFollowing().getTier();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getCreatedDatetime());
    }
}
