package com.trevorgowing.projectlog.common.persistence;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    private static final long serialVersionUID = 5662590973333935284L;

    @Id
    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private PK id;

    public AbstractPersistable(PK id) {
        this.id = id;
    }

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
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName() + "{");
        toStringBuilder.append("id=").append(id);
        toStringBuilder.append('}');
        return toStringBuilder.toString();
    }
}
