package site.bookmore.bookmore.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private int progress;

    @NotNull
    private boolean completed;

    @NotNull
    private LocalDate deadline;

    @NotNull
    private LocalDateTime createdDateTime;

    @NotNull
    private LocalDateTime LastModifiedDatetime;

    @NotNull
    private LocalDateTime deletedDatetime;

    public void update(Challenge update){
        this.title = update.title;
        this.description = update.getDescription();
    }
}
