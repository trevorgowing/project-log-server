package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.builders.AbstractEntityBuilder;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import com.trevorgowing.projectlog.log.constant.*;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.log.constant.Impact.MODERATE;
import static com.trevorgowing.projectlog.log.constant.LogStatus.NEW;
import static com.trevorgowing.projectlog.log.issue.Issue.completeIssue;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;

public class IssueBuilder extends AbstractEntityBuilder<Issue> {

    private Long id;
    private String summary = "Default Risk Summary";
    private String description;
    private Category category;
    private Impact impact = MODERATE;
    private LogStatus status = NEW;
    private LocalDate dateClosed;
    private Project project = aProject().build();
    private User owner = aUser().build();

    public static IssueBuilder anIssue() {
        return new IssueBuilder();
    }

    @Override
    public Issue build() {
        return completeIssue(id, summary, description, category, impact, status, dateClosed, project, owner);
    }

    @Override
    public AbstractEntityPersister<Issue> getPersister() {
        return new IssuePersister();
    }

    public IssueBuilder id(long id) {
        this.id = id;
        return this;
    }

    public IssueBuilder summary(String summary) {
        this.summary = summary;
        return this;
    }

    public IssueBuilder description(String description) {
        this.description = description;
        return this;
    }

    public IssueBuilder category(Category category) {
        this.category = category;
        return this;
    }

    public IssueBuilder impact(Impact impact) {
        this.impact = impact;
        return this;
    }

    public IssueBuilder status(LogStatus status) {
        this.status = status;
        return this;
    }

    public IssueBuilder dateClosed(LocalDate dateClosed) {
        this.dateClosed = dateClosed;
        return this;
    }

    public IssueBuilder project(Project project) {
        this.project = project;
        return this;
    }

    public IssueBuilder owner(User owner) {
        this.owner = owner;
        return this;
    }
}