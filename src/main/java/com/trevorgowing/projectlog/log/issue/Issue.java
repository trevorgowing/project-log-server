package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.*;
import com.trevorgowing.projectlog.log.risk.Risk;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("issue")
@NoArgsConstructor
public class Issue extends Log {

    private static final long serialVersionUID = 1984300132708207255L;

    private Issue(Long id, String summary, String description, Category category, Impact impact, LogStatus status,
                 LocalDate dateClosed, Project project, User owner) {
        super(id, summary, description, category, impact, status, dateClosed, project, owner);
    }

    public static Issue completeIssue(Long id, String summary, String description, Category category, Impact impact,
                                    LogStatus status, LocalDate dateClosed, Project project, User owner) {
        return new Issue(id, summary, description, category, impact, status, dateClosed, project, owner);
    }
}
