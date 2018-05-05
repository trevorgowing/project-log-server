package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.Risk.unidentifiedRisk;

import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.stereotype.Service;

@Service
public class RiskFactory {

  private final UserRetriever userRetriever;
  private final RiskRepository riskRepository;
  private final ProjectRetriever projectRetriever;

  public RiskFactory(
      UserRetriever userRetriever,
      RiskRepository riskRepository,
      ProjectRetriever projectRetriever) {
    this.userRetriever = userRetriever;
    this.riskRepository = riskRepository;
    this.projectRetriever = projectRetriever;
  }

  public Risk createRisk(UnidentifiedRiskDTO unidentifiedRiskDTO) {
    Project project = projectRetriever.findProject(unidentifiedRiskDTO.getProject().getId());
    User owner = userRetriever.findUser(unidentifiedRiskDTO.getOwner().getId());

    Risk risk =
        unidentifiedRisk(
            unidentifiedRiskDTO.getSummary(),
            unidentifiedRiskDTO.getDescription(),
            unidentifiedRiskDTO.getCategory(),
            unidentifiedRiskDTO.getImpact(),
            unidentifiedRiskDTO.getStatus(),
            unidentifiedRiskDTO.getDateClosed(),
            project,
            owner,
            unidentifiedRiskDTO.getProbability(),
            unidentifiedRiskDTO.getRiskResponse());

    return riskRepository.save(risk);
  }
}
