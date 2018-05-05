package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static java.util.Optional.ofNullable;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RiskRetriever implements LogRetriever {

  private final RiskRepository riskRepository;

  public RiskRetriever(RiskRepository riskRepository) {
    this.riskRepository = riskRepository;
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
}
