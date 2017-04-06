package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class UserResponseDTOBuilder extends AbstractUserDTOBuilder<UserResponseDTOBuilder>
        implements DomainObjectBuilder<UserResponseDTO> {

    private long id;

    public static UserResponseDTOBuilder aUserResponseDTO() {
        return new UserResponseDTOBuilder();
    }

    @Override
    public UserResponseDTO build() {
        return UserResponseDTO.completeUserResponseDTO(id, email, password, firstName, lastName);
    }

    public UserResponseDTOBuilder id(long id) {
        this.id = id;
        return this;
    }
}
