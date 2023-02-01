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
    private Integer progress;

    @NotNull
    private boolean completed;

    @NotNull
    private LocalDate deadline;

    public void update(Challenge challenge) {
        updateTitle(challenge.getTitle());
        updateDescription(challenge.getDescription());
        updateProgress(challenge.getProgress());
        updateDeadline(challenge.getDeadline());
    }

    private void updateTitle(String title){
        if (title != null) {
            this.title = title;
        }
    }

    private void updateDescription(String description){
        if(description != null){
            this.description = description;
        }
    }

    private void updateProgress(Integer progress){
        if(progress != null){
            this.progress = progress;
        }
    }

    private void updateDeadline(LocalDate deadline){
        if(deadline != null){
            this.deadline = deadline;
        }
    }
}
