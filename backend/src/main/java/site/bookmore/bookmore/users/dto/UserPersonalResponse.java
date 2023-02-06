package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

import java.time.LocalDate;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPersonalResponse {
    private String email;
    private String nickname;
    private LocalDate birth;
    private String profile;

    public UserPersonalResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.birth = user.getBirth();
    }

    public static UserPersonalResponse of(User user){
        return UserPersonalResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .profile(user.getProfile())
                .build();
    }
}
