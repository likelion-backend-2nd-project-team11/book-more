package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;
import site.bookmore.bookmore.users.dto.FollowerResponse;
import site.bookmore.bookmore.users.dto.FollowingResponse;

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
    @JoinColumn(name = "following", nullable = false)
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower", nullable = false)
    private User follower;

    public FollowerResponse toFollowerResponse() {
        return FollowerResponse.builder()
                .id(this.id)
                .follower(this.follower.getId())
                .createdDatetime(this.getCreatedDatetime())
                .lastModifiedDatetime(this.getLastModifiedDatetime())
                .build();
    }

    public FollowingResponse toFollowingResponse() {
        return FollowingResponse.builder()
                .id(this.id)
                .following(this.following.getId())
                .createdDatetime(this.getCreatedDatetime())
                .lastModifiedDatetime(this.getLastModifiedDatetime())
                .build();
    }
}
