package com.trevorgowing.projectlog.common.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DateRange implements Serializable {

  private static final long serialVersionUID = -6712791788356051692L;

  @Basic(optional = false)
  @Column(nullable = false)
  private LocalDate startDate;

  private LocalDate endDate;

  @SuppressWarnings("unused")
  public DateRange(LocalDate startDate) {
    this.startDate = startDate;
  }
}
