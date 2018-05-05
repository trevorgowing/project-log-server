package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import java.time.LocalDate;

@SuppressWarnings("unchecked")
public abstract class AbstractLogDTOBuilder<DOB extends DomainObjectBuilder> {

  protected String summary;
  protected String description;
  protected Category category;
  protected Impact impact;
  protected LogStatus status;
  protected LocalDate dateClosed;
  protected IdentifiedProjectDTO project;
  protected IdentifiedUserDTO owner;

  public DOB summary(String summary) {
    this.summary = summary;
    return (DOB) this;
  }

  public DOB description(String description) {
    this.description = description;
    return (DOB) this;
  }

  public DOB category(Category category) {
    this.category = category;
    return (DOB) this;
  }

  public DOB impact(Impact impact) {
    this.impact = impact;
    return (DOB) this;
  }

  public DOB status(LogStatus status) {
    this.status = status;
    return (DOB) this;
  }

  public DOB dateClosed(LocalDate dateClosed) {
    this.dateClosed = dateClosed;
    return (DOB) this;
  }

  public DOB project(IdentifiedProjectDTO project) {
    this.project = project;
    return (DOB) this;
  }

  public DOB owner(IdentifiedUserDTO owner) {
    this.owner = owner;
    return (DOB) this;
  }
}
