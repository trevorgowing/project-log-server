package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class UserRequestDTOBuilder extends AbstractUserDTOBuilder<UserRequestDTOBuilder>
        implements DomainObjectBuilder<UserRequestDTO>{

    public static UserRequestDTOBuilder aUserRequestDTO() {
        return new UserRequestDTOBuilder();
    }

    @Override
    public UserRequestDTO build() {
        return UserRequestDTO.completeUserRequest(email, password, firstName, lastName);
    }
}
