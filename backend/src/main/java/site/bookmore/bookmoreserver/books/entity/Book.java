package site.bookmore.bookmoreserver.books.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<Translator> translators = new ArrayList<>();

    @Column(nullable = false)
    private String kdc;

    @Column(nullable = false)
    private String publisher;

    private int pages;

    private String image;

    @Column(nullable = false)
    private String chapter;

    @Column(nullable = false)
    private String introduce;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private int price;

    @CreatedDate
    private LocalDateTime createdDatetime;
}
