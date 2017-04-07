package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class IdentifiedUserDTOBuilder extends AbstractUserDTOBuilder<IdentifiedUserDTOBuilder>
        implements DomainObjectBuilder<IdentifiedUserDTO> {

    private long id;

    public static IdentifiedUserDTOBuilder anIdentifiedUserDTO() {
        return new IdentifiedUserDTOBuilder();
    }

    @Override
    public IdentifiedUserDTO build() {
        return IdentifiedUserDTO.completeIdentifiedUserDTO(id, email, password, firstName, lastName);
    }

    public IdentifiedUserDTOBuilder id(long id) {
        this.id = id;
        return this;
    }
}
