package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogType;
import com.trevorgowing.projectlog.log.issue.IssueCRUDService;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.springframework.stereotype.Service;

@Service
class LogCRUDServiceFactory {

    private final RiskCRUDService riskCRUDService;
    private final IssueCRUDService issueCRUDService;
    private final CombinedLogCRUDService combinedLogCRUDService;

    LogCRUDServiceFactory(RiskCRUDService riskCRUDService, IssueCRUDService issueCRUDService,
                                 CombinedLogCRUDService combinedLogCRUDService) {
        this.riskCRUDService = riskCRUDService;
        this.issueCRUDService = issueCRUDService;
        this.combinedLogCRUDService = combinedLogCRUDService;
    }

    LogCRUDService getLogCRUDService() {
        return getLogCRUDService(null);
    }

    LogCRUDService getLogCRUDService(LogType type) {
        if (type != null) {
            switch (type) {
                case RISK:
                    return riskCRUDService;
                case ISSUE:
                    return issueCRUDService;
            }
        }
        return combinedLogCRUDService;
    }
}