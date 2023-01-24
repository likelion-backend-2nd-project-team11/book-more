package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserUpdateRequest {
    private String password;
    private String nickname;
    private LocalDate birth;

    public User toEntity(String encoder) {
        return User.builder()
                .password(encoder)
                .nickname(nickname)
                .birth(birth)
                .build();
    }

}
