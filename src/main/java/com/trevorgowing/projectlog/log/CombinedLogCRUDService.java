package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.issue.IssueCRUDService;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class CombinedLogCRUDService implements LogCRUDService {

    private final RiskCRUDService riskCRUDService;
    private final IssueCRUDService issueCRUDService;

    public CombinedLogCRUDService(RiskCRUDService riskCRUDService, IssueCRUDService issueCRUDService) {
        this.riskCRUDService = riskCRUDService;
        this.issueCRUDService = issueCRUDService;
    }

    @Override
    public List<LogDTO> getLogDTOs() {
        List<LogDTO> logDTOs = new ArrayList<>();
        logDTOs.addAll(riskCRUDService.getLogDTOs());
        logDTOs.addAll(issueCRUDService.getLogDTOs());
        return logDTOs;
    }
}