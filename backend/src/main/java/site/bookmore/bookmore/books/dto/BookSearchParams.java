package site.bookmore.bookmore.books.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookSearchParams {
    @NotBlank(message = "검색어를 입력해주세요.")
    @Length(min = 2, message = "최소 두 글자 이상 검색해주세요.")
    private String query;

    private Integer page;

    private Integer size;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }

    public static BookSearchParams of(String query) {
        return BookSearchParams.builder()
                .query(query)
                .build();
    }
}
