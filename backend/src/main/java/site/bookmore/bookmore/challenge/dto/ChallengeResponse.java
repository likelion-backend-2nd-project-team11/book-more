package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ChallengeResponse {
    private String message;
    private Long postId;

}
