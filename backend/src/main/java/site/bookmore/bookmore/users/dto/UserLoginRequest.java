package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserLoginRequest {
    @Email(message = "올바른 형식의 이메일 주소여야 합니다")
    private String email;
    @NotBlank(message = "패스워드를 입력해주세요")
    private String password;
}
