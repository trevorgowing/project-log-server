package com.trevorgowing.projectlog.common.types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public abstract class AbstractRepositoryIntegrationTests extends AbstractTests {

    @Autowired
    protected TestEntityManager entityManager;
}
