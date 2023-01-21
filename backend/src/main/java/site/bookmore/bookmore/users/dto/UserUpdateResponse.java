package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class UserUpdateResponse {

    private Long id;
    private String message;

    public static UserUpdateResponse of(User user) {
        return UserUpdateResponse.builder()
                .id(user.getId())
                .message("수정 완료 했습니다.")
                .build();
    }
}
