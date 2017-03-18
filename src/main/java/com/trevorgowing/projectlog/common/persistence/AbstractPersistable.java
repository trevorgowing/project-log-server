package com.trevorgowing.projectlog.common.persistence;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    @Id
    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private PK id;

    @Override
    public PK getId() {
        return id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return Objects.equals(id, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPersistable)) return false;
        AbstractPersistable<?> that = (AbstractPersistable<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + getId();
    }
}
