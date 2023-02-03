package site.bookmore.bookmore.books.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookSearchParams {
    @NotBlank(message = "검색어를 입력해주세요.")
    @Size(min = 1, message = "최소 한 글자 이상 검색해주세요.")
    private String query;

    @Min(1)
    @Builder.Default
    private Integer page = 1;

    @Min(20)
    @Builder.Default
    private Integer size = 20;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }

    public static BookSearchParams of(String query) {
        return BookSearchParams.builder()
                .query(query)
                .build();
    }
}
