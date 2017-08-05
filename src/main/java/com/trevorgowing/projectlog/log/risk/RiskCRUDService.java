package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.risk.Risk.unidentifiedRisk;
import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class RiskCRUDService implements LogRetriever {

    private final UserRetriever userRetriever;
    private final RiskRepository riskRepository;
    private final ProjectRetriever projectRetriever;

    public RiskCRUDService(UserRetriever userRetriever, RiskRepository riskRepository,
						   ProjectRetriever projectRetriever) {
        this.userRetriever = userRetriever;
        this.riskRepository = riskRepository;
        this.projectRetriever = projectRetriever;
    }

    public Risk findRisk(long riskId) {
        return ofNullable(riskRepository.findOne(riskId))
                .orElseThrow(() -> identifiedRiskNotFoundException(riskId));
    }

    @Override
    public List<LogDTO> getLogDTOs() {
        return new ArrayList<>(getIdentifiedRiskDTOs());
    }

    @Override
    public LogDTO getLogDTOById(long id) {
        return ofNullable(riskRepository.findIdentifiedRiskDTOById(id))
                .orElseThrow(() -> identifiedRiskNotFoundException(id));
    }

    List<IdentifiedRiskDTO> getIdentifiedRiskDTOs() {
        return riskRepository.findIdentifiedRiskDTOs();
    }

    public Risk createRisk(UnidentifiedRiskDTO unidentifiedRiskDTO) {
        Project project = projectRetriever.findProject(unidentifiedRiskDTO.getProject().getId());
        User owner = userRetriever.findUser(unidentifiedRiskDTO.getOwner().getId());

        Risk risk = unidentifiedRisk(unidentifiedRiskDTO.getSummary(), unidentifiedRiskDTO.getDescription(),
                unidentifiedRiskDTO.getCategory(), unidentifiedRiskDTO.getImpact(), unidentifiedRiskDTO.getStatus(),
                unidentifiedRiskDTO.getDateClosed(), project, owner, unidentifiedRiskDTO.getProbability(),
                unidentifiedRiskDTO.getRiskResponse());

        return riskRepository.save(risk);
    }

    public Risk updateRisk(IdentifiedRiskDTO identifiedRiskDTO) {
        Risk risk = findRisk(identifiedRiskDTO.getId());

        risk.setSummary(identifiedRiskDTO.getSummary());
        risk.setDescription(identifiedRiskDTO.getDescription());
        risk.setCategory(identifiedRiskDTO.getCategory());
        risk.setImpact(identifiedRiskDTO.getImpact());
        risk.setStatus(identifiedRiskDTO.getStatus());
        risk.setDateClosed(identifiedRiskDTO.getDateClosed());

        if (identifiedRiskDTO.getProject() != null && risk.getProject().getId() != identifiedRiskDTO.getProject().getId()) {
            Project project = projectRetriever.findProject(identifiedRiskDTO.getProject().getId());
            risk.setProject(project);
        }

        if (identifiedRiskDTO.getOwner() != null && risk.getOwner().getId() != identifiedRiskDTO.getOwner().getId()) {
            User owner = userRetriever.findUser(identifiedRiskDTO.getOwner().getId());
            risk.setOwner(owner);
        }

        risk.setProbability(identifiedRiskDTO.getProbability());
        risk.setRiskResponse(identifiedRiskDTO.getRiskResponse());

        return riskRepository.save(risk);
    }
}