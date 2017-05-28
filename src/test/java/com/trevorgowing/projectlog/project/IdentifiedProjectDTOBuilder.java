package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTO.completeIdentifiedProjectDTO;

public class IdentifiedProjectDTOBuilder extends AbstractProjectDTOBuilder<IdentifiedProjectDTOBuilder>
        implements DomainObjectBuilder<IdentifiedProjectDTO> {

    private long id;

    public static IdentifiedProjectDTOBuilder anIdentifiedProjectDTO() {
        return new IdentifiedProjectDTOBuilder();
    }

    @Override
    public IdentifiedProjectDTO build() {
        return completeIdentifiedProjectDTO(id, code, name, owner, startDate, endDate);
    }

    public IdentifiedProjectDTOBuilder id(long id) {
        this.id = id;
        return this;
    }

    public IdentifiedProjectDTOBuilder code(String code) {
        this.code = code;
        return this;
    }

    public IdentifiedProjectDTOBuilder name(String name) {
        this.name = name;
        return this;
    }

    public IdentifiedProjectDTOBuilder owner(IdentifiedUserDTO owner) {
        this.owner = owner;
        return this;
    }

    public IdentifiedProjectDTOBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public IdentifiedProjectDTOBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }
}