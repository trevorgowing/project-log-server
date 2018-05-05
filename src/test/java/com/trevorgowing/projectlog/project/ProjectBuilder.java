package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.Project.identifiedProject;

import com.trevorgowing.projectlog.common.builders.AbstractEntityBuilder;
import com.trevorgowing.projectlog.common.persistence.DateRange;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import com.trevorgowing.projectlog.user.User;
import java.time.LocalDate;

public class ProjectBuilder extends AbstractEntityBuilder<Project> {

  private Long id;
  private String code;
  private String name;
  private User owner;
  private DateRange dateRange = new DateRange(LocalDate.now());

  public static ProjectBuilder aProject() {
    return new ProjectBuilder();
  }

  @Override
  public Project build() {
    return identifiedProject(id, code, name, owner, dateRange);
  }

  @Override
  public AbstractEntityPersister<Project> getPersister() {
    return new ProjectPersister();
  }

  public ProjectBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public ProjectBuilder code(String code) {
    this.code = code;
    return this;
  }

  public ProjectBuilder name(String name) {
    this.name = name;
    return this;
  }

  public ProjectBuilder owner(User owner) {
    this.owner = owner;
    return this;
  }

  public ProjectBuilder startDate(LocalDate startDate) {
    this.dateRange.setStartDate(startDate);
    return this;
  }

  public ProjectBuilder endDate(LocalDate endDate) {
    this.dateRange.setEndDate(endDate);
    return this;
  }
}
