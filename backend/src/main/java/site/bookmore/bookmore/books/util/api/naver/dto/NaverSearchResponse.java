package site.bookmore.bookmore.books.util.api.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NaverSearchResponse {
    private Integer total;
    private Integer start;
    private Integer display;
    private List<Item> items;
}
