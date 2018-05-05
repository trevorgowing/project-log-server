package com.trevorgowing.projectlog.common.persistence;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

@Getter
@ToString
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

  private static final long serialVersionUID = 5662590973333935284L;

  @Id
  @Basic(optional = false)
  @Column(nullable = false, unique = true)
  @GeneratedValue
  private PK id;

  @Override
  @Transient
  public boolean isNew() {
    return Objects.isNull(id);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (!(objectToCompareTo instanceof AbstractPersistable)) return false;
    AbstractPersistable<?> persistableToCompareTo = (AbstractPersistable<?>) objectToCompareTo;
    return Objects.equals(id, persistableToCompareTo.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
