package com.trevorgowing.projectlog.common.persistence;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractAuditable<U, PK extends Serializable> extends AbstractPersistable<PK>
        implements Auditable<U, PK> {

    private static final long serialVersionUID = 6786219322987115149L;

    @ManyToOne(fetch = FetchType.LAZY)
    private U createdBy;

    @Basic(optional = false)
    @Column(nullable = false)
    @CreatedDate
    private Instant createdDate = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private U lastModifiedBy;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Override
    public U getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Instant getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public U getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
