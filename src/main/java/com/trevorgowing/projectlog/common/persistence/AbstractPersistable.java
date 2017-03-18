package com.trevorgowing.projectlog.common.persistence;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Setter
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    private static final long serialVersionUID = 5662590973333935284L;

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
}
