package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

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
}