package com.trevorgowing.projectlog.common.persistence;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.domain.Persistable;

public interface Auditable<U, ID extends Serializable> extends Persistable<ID> {

  /**
   * Returns the user who created this entity.
   *
   * @return the createdBy
   */
  U getCreatedBy();

  /**
   * Sets the user who created this entity.
   *
   * @param createdBy the creating entity to set
   */
  void setCreatedBy(final U createdBy);

  /**
   * Returns the creation date of the entity.
   *
   * @return the createdDate
   */
  Instant getCreatedDate();

  /**
   * Sets the creation date of the entity.
   *
   * @param createdDate the creation date to set
   */
  void setCreatedDate(final Instant createdDate);

  /**
   * Returns the user who modified the entity lastly.
   *
   * @return the lastModifiedBy
   */
  U getLastModifiedBy();

  /**
   * Sets the user who modified the entity lastly.
   *
   * @param lastModifiedBy the last modifying entity to set
   */
  void setLastModifiedBy(final U lastModifiedBy);

  /**
   * Returns the date of the last modification.
   *
   * @return the lastModifiedDate
   */
  Instant getLastModifiedDate();

  /**
   * Sets the date of the last modification.
   *
   * @param lastModifiedDate the date of the last modification to set
   */
  void setLastModifiedDate(final Instant lastModifiedDate);
}
