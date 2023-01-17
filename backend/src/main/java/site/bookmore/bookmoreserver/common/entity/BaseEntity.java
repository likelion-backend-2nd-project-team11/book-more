package site.bookmore.bookmoreserver.common.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    private LocalDateTime createdDatetime;

    @LastModifiedDate
    private LocalDateTime lastModifiedDatetime;

    private LocalDateTime deletedDatetime;

    public void delete() {
        this.deletedDatetime = LocalDateTime.now();
    }

    public void undelete() {
        this.deletedDatetime = null;
    }
}
