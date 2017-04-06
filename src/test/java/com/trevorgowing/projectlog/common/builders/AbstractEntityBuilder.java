package com.trevorgowing.projectlog.common.builders;

import com.trevorgowing.projectlog.common.persistence.AbstractPersistable;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;

import javax.persistence.EntityManager;

public abstract class AbstractEntityBuilder<E extends AbstractPersistable> implements DomainObjectBuilder<E> {

    public abstract AbstractEntityPersister<E> getPersister();

    public E buildAndPersist(EntityManager entityManager) {
        return getPersister().deepPersist(build(), entityManager);
    }
}
