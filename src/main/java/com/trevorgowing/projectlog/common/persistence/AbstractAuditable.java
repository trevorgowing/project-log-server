package com.trevorgowing.projectlog.common.persistence;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractAuditable<U, PK extends Serializable> extends AbstractPersistable<PK>
        implements Auditable<U, PK> {

    @ManyToOne
    private U createdBy;

    @Basic(optional = false)
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    private U lastModifiedBy;

    @Basic
    @Column
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Override
    public U getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
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
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
