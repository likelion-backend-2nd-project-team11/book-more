package site.bookmore.bookmore.books.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String label;

    @Builder
    public Tag(String label) {
        this.label = label;
    }

    public static Tag of(String label) {
        return Tag.builder().label(label).build();
    }

    public static Set<Tag> of(Set<String> labels) {
        return labels.stream().map(Tag::of).collect(Collectors.toSet());
    }
}
