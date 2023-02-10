package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserUpdateRequest {
    @Size(min = 8, message = "최소 8자리 이상의 패스워드를 입력해주세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 15, message = "15자리 이하의 닉네임을 입력해주세요.")
    private String nickname;
    @PastOrPresent(message = "올바른 생일을 입력해주세요.")
    private LocalDate birth;

    public User toEntity(String encoder) {
        return User.builder()
                .password(encoder)
                .nickname(nickname)
                .birth(birth)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .password(password)
                .nickname(nickname)
                .birth(birth)
                .build();
    }
}
