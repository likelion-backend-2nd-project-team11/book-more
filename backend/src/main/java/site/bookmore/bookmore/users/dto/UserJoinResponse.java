package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.bookmore.bookmore.users.entity.User;


@Builder
@AllArgsConstructor
@Getter
public class UserJoinResponse {
    private Long id;
    private String email;
    private String nickname;

    public static UserJoinResponse of(User user) {
        return UserJoinResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
