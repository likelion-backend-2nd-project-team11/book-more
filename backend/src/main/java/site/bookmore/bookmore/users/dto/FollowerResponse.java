package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.Follow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private Long follower;
    private String email;
    private String nickname;
    private LocalDate birth;
    private String createdDatetime;
    private String lastModifiedDatetime;

    public FollowerResponse(Follow follow) {
        this.id = follow.getId();
        this.follower = follow.getFollower().getId();
        this.email = follow.getFollower().getEmail();
        this.nickname = follow.getFollower().getNickname();
        this.birth = follow.getFollower().getBirth();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getCreatedDatetime());
        this.lastModifiedDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getLastModifiedDatetime());
    }
}
