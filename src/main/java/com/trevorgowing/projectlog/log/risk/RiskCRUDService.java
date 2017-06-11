package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectRepository;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.risk.Risk.unidentifiedRisk;
import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class RiskCRUDService implements LogRetriever {

    private final UserRepository userRepository;
    private final RiskRepository riskRepository;
    private final ProjectRepository projectRepository;

    public RiskCRUDService(UserRepository userRepository, RiskRepository riskRepository,
                           ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.riskRepository = riskRepository;
        this.projectRepository = projectRepository;
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
        Project project = ofNullable(projectRepository.findOne(unidentifiedRiskDTO.getProject().getId()))
                .orElseThrow(() -> identifiedProjectNotFoundException(unidentifiedRiskDTO.getProject().getId()));
        User owner = ofNullable(userRepository.findOne(unidentifiedRiskDTO.getOwner().getId()))
                .orElseThrow(() -> identifiedUserNotFoundException(unidentifiedRiskDTO.getOwner().getId()));

        Risk risk = unidentifiedRisk(unidentifiedRiskDTO.getSummary(), unidentifiedRiskDTO.getDescription(),
                unidentifiedRiskDTO.getCategory(), unidentifiedRiskDTO.getImpact(), unidentifiedRiskDTO.getStatus(),
                unidentifiedRiskDTO.getDateClosed(), project, owner, unidentifiedRiskDTO.getProbability(),
                unidentifiedRiskDTO.getRiskResponse());

        return riskRepository.save(risk);
    }
}