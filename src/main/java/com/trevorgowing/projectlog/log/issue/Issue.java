package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("issue")
@NoArgsConstructor
public class Issue extends Log {

    private static final long serialVersionUID = 1984300132708207255L;

    @SuppressWarnings("unused")
    public Issue(String summary, Impact impact, User owner, Project project) {
        super(summary, impact, owner, project);
    }
}
