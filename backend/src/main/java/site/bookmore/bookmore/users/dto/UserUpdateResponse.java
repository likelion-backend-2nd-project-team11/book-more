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
public class UserUpdateResponse {
    private String email;
    private String nickname;
    private LocalDate birth;

    public UserUpdateResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.birth = user.getBirth();
    }

    public static UserUpdateResponse of(User user){
        return UserUpdateResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .build();
    }
}
