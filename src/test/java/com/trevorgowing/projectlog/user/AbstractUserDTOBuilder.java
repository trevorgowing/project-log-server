package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

@SuppressWarnings("unchecked")
public abstract class AbstractUserDTOBuilder<DOB extends DomainObjectBuilder> {

    String email;
    String password;
    String firstName;
    String lastName;

    public DOB email(String email) {
        this.email = email;
        return (DOB) this;
    }

    public DOB password(String password) {
        this.password = password;
        return (DOB) this;
    }

    public DOB firstName(String firstName) {
        this.firstName = firstName;
        return (DOB) this;
    }

    public DOB lastName(String lastName) {
        this.lastName = lastName;
        return (DOB) this;
    }
}
