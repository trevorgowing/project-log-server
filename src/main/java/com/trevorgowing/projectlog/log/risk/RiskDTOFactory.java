package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO.completeIdentifiedRiskDTO;

import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.ProjectDTOFactory;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.UserDTOFactory;
import org.springframework.stereotype.Service;

@Service
public class RiskDTOFactory {

  private final UserDTOFactory userDTOFactory;
  private final ProjectDTOFactory projectDTOFactory;

  public RiskDTOFactory(UserDTOFactory userDTOFactory, ProjectDTOFactory projectDTOFactory) {
    this.userDTOFactory = userDTOFactory;
    this.projectDTOFactory = projectDTOFactory;
  }

  public IdentifiedRiskDTO createIdentifiedRiskDTO(Risk risk) {
    IdentifiedUserDTO identifiedUserDTO = userDTOFactory.createIdentifiedUserDTO(risk.getOwner());
    IdentifiedProjectDTO identifiedProjectDTO =
        projectDTOFactory.createIdentifiedProjectDTO(risk.getProject());

    return completeIdentifiedRiskDTO(
        risk.getId(),
        risk.getSummary(),
        risk.getDescription(),
        risk.getCategory(),
        risk.getImpact(),
        risk.getStatus(),
        risk.getDateClosed(),
        identifiedProjectDTO,
        identifiedUserDTO,
        risk.getProbability(),
        risk.getRiskResponse());
  }
}
