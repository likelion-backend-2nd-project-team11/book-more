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
public class UserResponse {

    private Long id;
    private String message;

    public static UserResponse of(User user, String message) {
        return UserResponse.builder()
                .id(user.getId())
                .message(message)
                .build();
    }
}
