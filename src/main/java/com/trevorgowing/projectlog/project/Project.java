package com.trevorgowing.projectlog.project;

import static java.util.Optional.ofNullable;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;
import com.trevorgowing.projectlog.common.persistence.DateRange;
import com.trevorgowing.projectlog.common.persistence.HasDateRange;
import com.trevorgowing.projectlog.common.persistence.ValidatedDateRange;
import com.trevorgowing.projectlog.user.User;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class Project extends AbstractAuditable<User, Long> implements HasDateRange {

  private static final long serialVersionUID = -2208694199836258417L;

  @Basic(optional = false)
  @Column(nullable = false, unique = true)
  private String code;

  private String name;

  @ManyToOne(optional = false)
  private User owner;

  @Basic(optional = false)
  @Column(nullable = false)
  @Embedded
  @ValidatedDateRange
  private DateRange dateRange;

  private Project(Long id, String code, String name, User owner, DateRange dateRange) {
    super(id);
    this.code = code;
    this.name = name;
    this.owner = owner;
    this.dateRange = dateRange;
  }

  public static Project unidentifiedProject(
      String code, String name, User owner, DateRange dateRange) {
    return new Project(code, name, owner, dateRange);
  }

  public static Project unidentifiedProject(
      String code, String name, User owner, LocalDate startDate, LocalDate endDate) {
    return new Project(code, name, owner, new DateRange(startDate, endDate));
  }

  public static Project identifiedProject(
      Long id, String code, String name, User owner, DateRange dateRange) {
    return new Project(id, code, name, owner, dateRange);
  }

  @Override
  public LocalDate getStartDate() {
    return dateRange == null ? null : dateRange.getStartDate();
  }

  @Override
  public void setStartDate(LocalDate startDate) {
    ensureDateRange();
    dateRange.setStartDate(startDate);
  }

  @Override
  public LocalDate getEndDate() {
    return dateRange == null ? null : dateRange.getEndDate();
  }

  @Override
  public void setEndDate(LocalDate endDate) {
    ensureDateRange();
    dateRange.setEndDate(endDate);
  }

  @Override
  public void ensureDateRange() {
    dateRange = ofNullable(dateRange).isPresent() ? dateRange : new DateRange();
  }
}
