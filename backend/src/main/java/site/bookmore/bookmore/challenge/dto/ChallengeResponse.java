package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.bookmore.bookmore.challenge.entity.Challenge;

@AllArgsConstructor
@Builder
@Getter
public class ChallengeResponse {
    private String message;
    private Long challengeId;

    public static ChallengeResponse of(Challenge challenge, String message){
        return ChallengeResponse.builder()
                .challengeId(challenge.getId())
                .message(message)
                .build();
    }
}
