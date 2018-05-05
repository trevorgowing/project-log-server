package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.stereotype.Service;

@Service
public class RiskModifier {

  private final RiskRetriever riskRetriever;
  private final UserRetriever userRetriever;
  private final RiskRepository riskRepository;
  private final ProjectRetriever projectRetriever;

  public RiskModifier(
      RiskRetriever riskRetriever,
      UserRetriever userRetriever,
      RiskRepository riskRepository,
      ProjectRetriever projectRetriever) {
    this.riskRetriever = riskRetriever;
    this.userRetriever = userRetriever;
    this.riskRepository = riskRepository;
    this.projectRetriever = projectRetriever;
  }

  public Risk updateRisk(IdentifiedRiskDTO identifiedRiskDTO) {
    Risk risk = riskRetriever.findRisk(identifiedRiskDTO.getId());

    risk.setSummary(identifiedRiskDTO.getSummary());
    risk.setDescription(identifiedRiskDTO.getDescription());
    risk.setCategory(identifiedRiskDTO.getCategory());
    risk.setImpact(identifiedRiskDTO.getImpact());
    risk.setStatus(identifiedRiskDTO.getStatus());
    risk.setDateClosed(identifiedRiskDTO.getDateClosed());

    if (identifiedRiskDTO.getProject() != null
        && risk.getProject().getId() != identifiedRiskDTO.getProject().getId()) {
      Project project = projectRetriever.findProject(identifiedRiskDTO.getProject().getId());
      risk.setProject(project);
    }

    if (identifiedRiskDTO.getOwner() != null
        && risk.getOwner().getId() != identifiedRiskDTO.getOwner().getId()) {
      User owner = userRetriever.findUser(identifiedRiskDTO.getOwner().getId());
      risk.setOwner(owner);
    }

    risk.setProbability(identifiedRiskDTO.getProbability());
    risk.setRiskResponse(identifiedRiskDTO.getRiskResponse());

    return riskRepository.save(risk);
  }
}
