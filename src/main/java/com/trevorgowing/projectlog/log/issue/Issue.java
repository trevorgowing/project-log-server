package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("issue")
public class Issue extends Log {

    public Issue(String summary, Impact impact, User owner, Project project) {
        super(summary, impact, owner, project);
    }
}
