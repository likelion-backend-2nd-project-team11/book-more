package site.bookmore.bookmore.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ChallengeListResponse {
    private List<ChallengeDetailResponse> content;
    private Pageable pageable;

}
