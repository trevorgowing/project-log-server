package com.trevorgowing.projectlog.common.persistence;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditable<U, PK extends Serializable> extends AbstractPersistable<PK>
    implements Auditable<U, PK> {

  private static final long serialVersionUID = 6786219322987115149L;

  @JoinColumn(name = "app_created_by_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private U createdBy;

  @Basic(optional = false)
  @Column(nullable = false, name = "app_created_date")
  @CreatedDate
  private Instant createdDate;

  @JoinColumn(name = "app_last_modified_by_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private U lastModifiedBy;

  @Column(name = "app_last_modified_date")
  @LastModifiedDate
  private Instant lastModifiedDate;

  protected AbstractAuditable(PK id) {
    super(id);
  }
}
