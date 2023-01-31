package site.bookmore.bookmore.books.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_translator")
public class Translator {
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

    public static Translator of(String name) {
        return Translator.builder()
                .name(name)
                .build();
    }
}
