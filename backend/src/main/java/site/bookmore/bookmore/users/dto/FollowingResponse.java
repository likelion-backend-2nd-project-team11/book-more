package site.bookmore.bookmore.users.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class FollowingResponse {
    private Long id;
    private Long following;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastModifiedDatetime;
}
