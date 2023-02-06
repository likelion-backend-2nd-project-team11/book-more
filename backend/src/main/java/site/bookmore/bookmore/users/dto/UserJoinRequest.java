package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinRequest {
    @Email(message = "올바른 형식의 이메일 주소여야 합니다")
    private String email;
    private String password;
    private String nickname;
    private LocalDate birth;

    public User toEntity(String encoder) {
        return User.builder()
                .email(email)
                .password(encoder)
                .nickname(nickname)
                .birth(birth)
                .build();
    }
}
