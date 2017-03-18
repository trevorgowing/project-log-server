package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "logs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
@Getter
@Setter
public abstract class Log extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = -4545843312415873005L;

    @Lob
    @Basic(optional = false)
    @Column(nullable = false)
    private String summary;

    @Lob
    private String description;

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

    private LocalDate dateClosed;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne(optional = false)
    private User owner;

    protected Log(String summary, Impact impact, User owner, Project project) {
        this.summary = summary;
        this.impact = impact;
        this.owner = owner;
        this.project = project;
    }
}
