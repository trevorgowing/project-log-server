package com.trevorgowing.projectlog.common.persistence;

import java.time.LocalDate;

public interface HasDateRange {

  LocalDate getStartDate();

  void setStartDate(LocalDate startDate);

  LocalDate getEndDate();

  void setEndDate(LocalDate endDate);

  void ensureDateRange();
}
