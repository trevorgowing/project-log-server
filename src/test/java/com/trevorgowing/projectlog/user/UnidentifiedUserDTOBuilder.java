package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class UnidentifiedUserDTOBuilder extends AbstractUserDTOBuilder<UnidentifiedUserDTOBuilder>
        implements DomainObjectBuilder<UnidentifiedUserDTO>{

    public static UnidentifiedUserDTOBuilder anUnidentifiedUserDTO() {
        return new UnidentifiedUserDTOBuilder();
    }

    @Override
    public UnidentifiedUserDTO build() {
        return UnidentifiedUserDTO.completeUnidentifiedUser(email, password, firstName, lastName);
    }
}
