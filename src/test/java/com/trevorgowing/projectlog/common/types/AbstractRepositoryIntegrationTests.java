package com.trevorgowing.projectlog.common.types;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DataJpaTest
public abstract class AbstractRepositoryIntegrationTests extends AbstractTests {

    @PersistenceContext
    protected EntityManager entityManager;
}
