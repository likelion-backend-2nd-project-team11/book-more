package site.bookmore.bookmore.books.util.api.kolis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KolisSearchResponse {
    @JsonProperty(value = "PAGE_NO")
    private String page;
    @JsonProperty(value = "TOTAL_COUNT")
    private String totalCount;
    private List<Doc> docs;

    public long getTotalCount() {
        return Long.parseLong(totalCount);
    }
}
