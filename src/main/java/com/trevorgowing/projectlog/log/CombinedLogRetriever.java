package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.issue.Issue;
import com.trevorgowing.projectlog.log.issue.IssueCRUDService;
import com.trevorgowing.projectlog.log.issue.IssueDTOFactory;
import com.trevorgowing.projectlog.log.risk.Risk;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import com.trevorgowing.projectlog.log.risk.RiskDTOFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.LogNotFoundException.identifiedLogNotFoundException;
import static java.util.Optional.ofNullable;

@Service
class CombinedLogRetriever implements LogRetriever {

    private final LogRepository logRepository;
    private final RiskDTOFactory riskDTOFactory;
    private final RiskCRUDService riskCRUDService;
    private final IssueDTOFactory issueDTOFactory;
    private final IssueCRUDService issueCRUDService;

    public CombinedLogRetriever(LogRepository logRepository, RiskDTOFactory riskDTOFactory,
                                RiskCRUDService riskCRUDService, IssueDTOFactory issueDTOFactory,
                                IssueCRUDService issueCRUDService) {
        this.logRepository = logRepository;
        this.riskDTOFactory = riskDTOFactory;
        this.riskCRUDService = riskCRUDService;
        this.issueDTOFactory = issueDTOFactory;
        this.issueCRUDService = issueCRUDService;
    }

    @Override
    public List<LogDTO> getLogDTOs() {
        List<LogDTO> logDTOs = new ArrayList<>();
        logDTOs.addAll(riskCRUDService.getLogDTOs());
        logDTOs.addAll(issueCRUDService.getLogDTOs());
        return logDTOs;
    }

    @Override
    public LogDTO getLogDTOById(long logId) {
        Log log = ofNullable(logRepository.findOne(logId))
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