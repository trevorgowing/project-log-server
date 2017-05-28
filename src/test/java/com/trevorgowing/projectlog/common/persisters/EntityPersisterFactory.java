package com.trevorgowing.projectlog.common.persisters;

import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectBuilder;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserBuilder;

public class EntityPersisterFactory {

    public static AbstractEntityPersister<User> aUserPersister() {
        return new UserBuilder().getPersister();
    }

    public static AbstractEntityPersister<Project> aProjectPersister() {
        return new ProjectBuilder().getPersister();
    }
}
