package site.bookmore.bookmore.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;
    private String email;
    private String nickname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String profile;

    public UserPersonalResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.birth = user.getBirth();
        this.profile = user.getProfile();
    }

    public static UserPersonalResponse of(User user){
        return UserPersonalResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .profile(user.getProfile())
                .build();
    }
}
