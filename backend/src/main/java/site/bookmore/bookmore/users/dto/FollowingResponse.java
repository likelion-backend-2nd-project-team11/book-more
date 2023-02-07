package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.Follow;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowingResponse {
    private Long id;
    private Long followingId;
    private String nickname;
    private String profile;
    private Integer followerCount;
    private Integer followingCount;
    private String createdDatetime;

    public FollowingResponse(Follow follow) {
        this.id = follow.getId();
        this.followingId = follow.getFollowing().getId();
        this.nickname = follow.getFollowing().getNickname();
        this.profile = follow.getFollowing().getProfile();
        this.followerCount = follow.getFollowing().getFollowCount().getFollowerCount();
        this.followingCount = follow.getFollowing().getFollowCount().getFollowingCount();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getCreatedDatetime());
    }
}
