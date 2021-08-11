package study.datajpa.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return this.createdDate == null;
    }
}
