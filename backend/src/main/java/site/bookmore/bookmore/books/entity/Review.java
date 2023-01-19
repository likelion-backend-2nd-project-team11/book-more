package site.bookmore.bookmore.books.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    private String body;
    private boolean spoiler;
    private int professionalism;
    private int fun;
    private int readability;
    private int collectible;
    private int difficulty;
    private int likesCount;

    public synchronized void likes() {
        likesCount++;
    }

    public synchronized void unlikes() {
        if (likesCount > 0) {
            likesCount--;
        }
    }
}