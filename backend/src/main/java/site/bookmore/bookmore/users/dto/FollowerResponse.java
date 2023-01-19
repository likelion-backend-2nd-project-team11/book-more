package site.bookmore.bookmore.users.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class FollowerResponse {
    private Long id;
    private Long follower;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastModifiedDatetime;
}
