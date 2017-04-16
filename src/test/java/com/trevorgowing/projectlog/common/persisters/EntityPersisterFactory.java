package com.trevorgowing.projectlog.common.persisters;

import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserBuilder;

public class EntityPersisterFactory {

    public static AbstractEntityPersister<User> aUserPersister() {
        return new UserBuilder().getPersister();
    }
}
