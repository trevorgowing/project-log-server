package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.projectlog.common.persisters.EntityPersisterFactory.aProjectPersister;
import static com.trevorgowing.projectlog.common.persisters.EntityPersisterFactory.aUserPersister;

import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import javax.persistence.EntityManager;

public class IssuePersister extends AbstractEntityPersister<Issue> {

  @Override
  public Issue deepPersist(Issue issueToPersist, EntityManager entityManager) {
    aProjectPersister().deepPersist(issueToPersist.getProject(), entityManager);
    aUserPersister().deepPersist(issueToPersist.getOwner(), entityManager);
    entityManager.persist(issueToPersist);
    return issueToPersist;
  }
}
