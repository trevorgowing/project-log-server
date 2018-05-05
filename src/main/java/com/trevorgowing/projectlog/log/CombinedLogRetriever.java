package com.trevorgowing.projectlog.log;

import static com.trevorgowing.projectlog.log.LogNotFoundException.identifiedLogNotFoundException;
import static java.util.Optional.ofNullable;

import com.trevorgowing.projectlog.log.issue.Issue;
import com.trevorgowing.projectlog.log.issue.IssueDTOFactory;
import com.trevorgowing.projectlog.log.issue.IssueRetriever;
import com.trevorgowing.projectlog.log.risk.Risk;
import com.trevorgowing.projectlog.log.risk.RiskDTOFactory;
import com.trevorgowing.projectlog.log.risk.RiskRetriever;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CombinedLogRetriever implements LogRetriever {

  private final LogRepository logRepository;
  private final RiskRetriever riskRetriever;
  private final IssueRetriever issueRetriever;
  private final RiskDTOFactory riskDTOFactory;
  private final IssueDTOFactory issueDTOFactory;

  @Override
  public List<LogDTO> getLogDTOs() {
    List<LogDTO> logDTOs = new ArrayList<>();
    logDTOs.addAll(riskRetriever.getLogDTOs());
    logDTOs.addAll(issueRetriever.getLogDTOs());
    return logDTOs;
  }

  @Override
  public LogDTO getLogDTOById(long logId) {
    Log log =
        ofNullable(logRepository.findOne(logId))
            .orElseThrow(() -> identifiedLogNotFoundException(logId));

    LogDTO logDTO = null;

    if (log instanceof Risk) {
      logDTO = riskDTOFactory.createIdentifiedRiskDTO((Risk) log);
    }

    if (log instanceof Issue) {
      logDTO = issueDTOFactory.createIdentifiedIssueDTO((Issue) log);
    }

    return logDTO;
  }
}
