package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following", nullable = false, foreignKey = @ForeignKey(name = "fk_follow_following_user"))
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower", nullable = false, foreignKey = @ForeignKey(name = "fk_follow_follower_user"))
    private User follower;
}
