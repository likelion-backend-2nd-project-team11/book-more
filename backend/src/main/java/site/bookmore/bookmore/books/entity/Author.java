package site.bookmore.bookmore.books.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_author", uniqueConstraints = {@UniqueConstraint(name = "idx_name_book", columnNames = {"name", "book_id"})})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    public void setBook(Book book) {
        this.book = book;
    }

    public static Author of(String name) {
        return Author.builder()
                .name(name)
                .build();
    }
}
