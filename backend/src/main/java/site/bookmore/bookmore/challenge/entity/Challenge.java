package site.bookmore.bookmore.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Challenge extends BaseEntity {
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

    public void update(Challenge update) {
        this.title = update.title;
        this.description = update.getDescription();
    }
}
