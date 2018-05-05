package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.common.persisters.EntityPersisterFactory.aUserPersister;
import static java.util.Optional.ofNullable;

import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import javax.persistence.EntityManager;

public class ProjectPersister extends AbstractEntityPersister<Project> {

  @Override
  public Project deepPersist(Project projectToPersist, EntityManager entityManager) {
    if (ofNullable(projectToPersist.getOwner()).isPresent()) {
      aUserPersister().deepPersist(projectToPersist.getOwner(), entityManager);
    }
    entityManager.persist(projectToPersist);
    return projectToPersist;
  }
}
