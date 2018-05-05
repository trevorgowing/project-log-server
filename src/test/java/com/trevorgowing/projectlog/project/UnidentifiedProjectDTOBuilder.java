package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.UnidentifiedProjectDTO.completeUnidentifiedProjectDTO;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

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
