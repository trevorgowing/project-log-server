package com.trevorgowing.projectlog.common.persisters;

import com.trevorgowing.projectlog.common.persistence.AbstractPersistable;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class DefaultEntityPersister<E extends AbstractPersistable> extends AbstractEntityPersister<E> {

    @Override
    public E deepPersist(E entityToPersist, TestEntityManager entityManager) {
        return entityManager.persist(entityToPersist);
    }
}
