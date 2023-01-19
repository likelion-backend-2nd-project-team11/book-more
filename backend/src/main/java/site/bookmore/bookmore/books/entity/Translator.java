package site.bookmore.bookmore.books.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "book_translator")
public class Translator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;
}
