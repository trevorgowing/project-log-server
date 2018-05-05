package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.common.persisters.EntityPersisterFactory.aProjectPersister;
import static com.trevorgowing.projectlog.common.persisters.EntityPersisterFactory.aUserPersister;

import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import javax.persistence.EntityManager;

public class RiskPersister extends AbstractEntityPersister<Risk> {

  @Override
  public Risk deepPersist(Risk riskToPersist, EntityManager entityManager) {
    aProjectPersister().deepPersist(riskToPersist.getProject(), entityManager);
    aUserPersister().deepPersist(riskToPersist.getOwner(), entityManager);
    entityManager.persist(riskToPersist);
    return riskToPersist;
  }
}
