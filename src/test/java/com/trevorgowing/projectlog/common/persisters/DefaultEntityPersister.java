package com.trevorgowing.projectlog.common.persisters;

import com.trevorgowing.projectlog.common.persistence.AbstractPersistable;

import javax.persistence.EntityManager;

public class DefaultEntityPersister<E extends AbstractPersistable> extends AbstractEntityPersister<E> {

    @Override
    public E deepPersist(E entityToPersist, EntityManager entityManager) {
        entityManager.persist(entityToPersist);
        return entityToPersist;
    }
}
