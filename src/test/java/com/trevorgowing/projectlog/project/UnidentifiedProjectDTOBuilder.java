package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.project.UnidentifiedProjectDTO.completeUnidentifiedProjectDTO;

class UnidentifiedProjectDTOBuilder extends AbstractProjectDTOBuilder<UnidentifiedProjectDTOBuilder>
        implements DomainObjectBuilder<UnidentifiedProjectDTO> {

    static UnidentifiedProjectDTOBuilder anUnidentifiedProjectDTO() {
        return new UnidentifiedProjectDTOBuilder();
    }

    @Override
    public UnidentifiedProjectDTO build() {
        return completeUnidentifiedProjectDTO(code, name, owner, startDate, endDate);
    }

    UnidentifiedProjectDTOBuilder code(String code) {
        this.code = code;
        return this;
    }

    UnidentifiedProjectDTOBuilder name(String name) {
        this.name = name;
        return this;
    }

    UnidentifiedProjectDTOBuilder owner(IdentifiedUserDTO owner) {
        this.owner = owner;
        return this;
    }

    UnidentifiedProjectDTOBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    UnidentifiedProjectDTOBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }
}