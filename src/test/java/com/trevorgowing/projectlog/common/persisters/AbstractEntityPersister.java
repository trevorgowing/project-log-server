package com.trevorgowing.projectlog.common.persisters;

import com.trevorgowing.projectlog.common.persistence.AbstractPersistable;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public abstract class AbstractEntityPersister<E extends AbstractPersistable> {

    public abstract E deepPersist(E entityToPersist, TestEntityManager entityManager);
}
