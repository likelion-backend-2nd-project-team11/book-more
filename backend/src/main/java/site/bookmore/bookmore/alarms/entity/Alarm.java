package site.bookmore.bookmore.alarms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Alarms")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alarmType;

    @Column(name = "source_id", nullable = false)
    private Long source;

    @Column(nullable = false)
    private boolean confirmed;

    @CreatedDate
    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User target_User;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User from_User;


}