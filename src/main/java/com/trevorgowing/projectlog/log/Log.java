package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "logs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Log extends AbstractAuditable<User, Long> {

    @Basic(optional = false)
    @Column(nullable = false)
    private String summary;

    @Basic(optional = true)
    @Column(nullable = true)
    private String description;

    @Basic(optional = true)
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Impact impact;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LogStatus status = LogStatus.NEW;

    @Basic(optional = true)
    @Column(nullable = true)
    private LocalDate dateClosed;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne(optional = false)
    private User owner;

    public Log(String summary, Impact impact, User owner, Project project) {
        this.summary = summary;
        this.impact = impact;
        this.owner = owner;
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Impact getImpact() {
        return impact;
    }

    public void setImpact(Impact impact) {
        this.impact = impact;
    }

    public LogStatus getStatus() {
        return status;
    }

    public void setStatus(LogStatus status) {
        this.status = status;
    }

    public LocalDate getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(LocalDate dateClosed) {
        this.dateClosed = dateClosed;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
