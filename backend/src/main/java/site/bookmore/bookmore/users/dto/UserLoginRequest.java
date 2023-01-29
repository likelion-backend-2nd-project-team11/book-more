package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserLoginRequest {
    @Email(message = "올바른 형식의 이메일 주소여야 합니다")
    private String email;
    private String password;
}
