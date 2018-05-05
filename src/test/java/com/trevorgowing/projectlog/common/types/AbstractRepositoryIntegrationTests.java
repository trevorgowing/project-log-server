package com.trevorgowing.projectlog.common.types;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.experimental.categories.Category;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Category(RepositoryIntegrationTests.class)
public abstract class AbstractRepositoryIntegrationTests extends AbstractSpringTests {

  @PersistenceContext protected EntityManager entityManager;
}
