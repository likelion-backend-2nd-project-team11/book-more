package site.bookmore.bookmore.books.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Tag;

@Getter
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private String label;

    @Builder
    public TagResponse(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public static TagResponse of(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .label(tag.getLabel())
                .build();
    }
}
