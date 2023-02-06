package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private Long id;
    private String nickname;
    private Integer followingCount;
    private Integer followerCount;

    public static UserDetailResponse of(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .followingCount(user.getFollowCount().getFollowingCount())
                .followerCount(user.getFollowCount().getFollowerCount())
                .build();
    }
}
