package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private long total_count;
    private long pageable_count;
    private boolean is_end;
}
