package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.Getter;

@Getter
public class Meta {
    private long total_count;
    private long pageable_count;
    private boolean is_end;
}
