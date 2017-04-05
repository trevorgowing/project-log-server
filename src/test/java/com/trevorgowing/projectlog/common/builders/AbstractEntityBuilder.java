package com.trevorgowing.projectlog.common.builders;

import com.trevorgowing.projectlog.common.persistence.AbstractPersistable;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public abstract class AbstractEntityBuilder<E extends AbstractPersistable> implements DomainObjectBuilder<E> {

    abstract AbstractEntityPersister<E> getPersister();

    abstract E buildAndPersist(TestEntityManager entityManager);

    protected E deepPersist(E entityToPersist, TestEntityManager entityManager) {
        return getPersister().deepPersist(entityToPersist, entityManager);
    }
}
