package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.builders.AbstractEntityBuilder;
import com.trevorgowing.projectlog.common.persistence.DateRange;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import com.trevorgowing.projectlog.user.User;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.project.Project.identifiedProject;

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

    ProjectBuilder id(Long id) {
        this.id = id;
        return this;
    }

    ProjectBuilder code(String code) {
        this.code = code;
        return this;
    }

    ProjectBuilder name(String name) {
        this.name = name;
        return this;
    }

    ProjectBuilder owner(User owner) {
        this.owner = owner;
        return this;
    }

    ProjectBuilder startDate(LocalDate startDate) {
        this.dateRange.setStartDate(startDate);
        return this;
    }

    ProjectBuilder endDate(LocalDate endDate) {
        this.dateRange.setEndDate(endDate);
        return this;
    }
}