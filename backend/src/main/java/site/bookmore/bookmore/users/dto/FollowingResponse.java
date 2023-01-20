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
public class FollowingResponse {
    private Long id;
    private Long following;
    private String email;
    private String nickname;
    private LocalDate birth;
    private String createdDatetime;
    private String lastModifiedDatetime;

    public FollowingResponse(Follow follow) {
        this.id = follow.getId();
        this.following = follow.getFollowing().getId();
        this.email = follow.getFollowing().getEmail();
        this.nickname = follow.getFollowing().getNickname();
        this.birth = follow.getFollowing().getBirth();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getCreatedDatetime());
        this.lastModifiedDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getLastModifiedDatetime());
    }
}
