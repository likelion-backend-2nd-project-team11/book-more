package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.Follow;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private Long follower;
    private String createdDatetime;
    private String lastModifiedDatetime;

    public FollowerResponse(Follow follow) {
        this.id = follow.getId();
        this.follower = follow.getFollower().getId();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getCreatedDatetime());
        this.lastModifiedDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(follow.getLastModifiedDatetime());
    }
}
