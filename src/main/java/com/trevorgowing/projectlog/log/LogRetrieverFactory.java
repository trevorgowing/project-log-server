package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogType;
import com.trevorgowing.projectlog.log.issue.IssueCRUDService;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.springframework.stereotype.Service;

@Service
class LogRetrieverFactory {

    private final RiskCRUDService riskCRUDService;
    private final IssueCRUDService issueCRUDService;
    private final CombinedLogRetriever combinedLogRetriever;

    LogRetrieverFactory(RiskCRUDService riskCRUDService, IssueCRUDService issueCRUDService,
                        CombinedLogRetriever combinedLogRetriever) {
        this.riskCRUDService = riskCRUDService;
        this.issueCRUDService = issueCRUDService;
        this.combinedLogRetriever = combinedLogRetriever;
    }

    LogRetriever getLogLookupService() {
        return getLogLookupService(null);
    }

    LogRetriever getLogLookupService(LogType type) {
        if (type != null) {
            switch (type) {
                case RISK:
                    return riskCRUDService;
                case ISSUE:
                    return issueCRUDService;
            }
        }

        return combinedLogRetriever;
    }
}