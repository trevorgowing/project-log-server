package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import java.time.LocalDate;

@SuppressWarnings("unchecked")
abstract class AbstractProjectDTOBuilder<DOB extends DomainObjectBuilder> {

  String code;
  String name;
  IdentifiedUserDTO owner;
  LocalDate startDate;
  LocalDate endDate;

  DOB code(String code) {
    this.code = code;
    return (DOB) this;
  }

  DOB name(String name) {
    this.name = name;
    return (DOB) this;
  }

  DOB owner(IdentifiedUserDTO owner) {
    this.owner = owner;
    return (DOB) this;
  }

  DOB startDate(LocalDate startDate) {
    this.startDate = startDate;
    return (DOB) this;
  }

  DOB endDate(LocalDate endDate) {
    this.endDate = endDate;
    return (DOB) this;
  }
}
