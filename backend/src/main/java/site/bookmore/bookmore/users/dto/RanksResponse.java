package site.bookmore.bookmore.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.bookmore.bookmore.users.entity.Ranks;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RanksResponse {

    private Long id;
    private Integer point;
    private Long ranking;


    public RanksResponse(Ranks ranks) {
        this.id = ranks.getId();
        this.point = ranks.getPoint();
        this.ranking = ranks.getRanking();

    }
    
}