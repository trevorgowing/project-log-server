package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogType;
import com.trevorgowing.projectlog.log.issue.IssueRetriever;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.springframework.stereotype.Service;

@Service
class LogRetrieverFactory {

    private final RiskCRUDService riskCRUDService;
    private final IssueRetriever issueRetriever;
    private final CombinedLogRetriever combinedLogRetriever;

    LogRetrieverFactory(RiskCRUDService riskCRUDService, IssueRetriever issueRetriever,
						CombinedLogRetriever combinedLogRetriever) {
        this.riskCRUDService = riskCRUDService;
        this.issueRetriever = issueRetriever;
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
                    return issueRetriever;
            }
        }

        return combinedLogRetriever;
    }
}