package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogType;
import com.trevorgowing.projectlog.log.issue.IssueRetriever;
import com.trevorgowing.projectlog.log.risk.RiskRetriever;
import org.springframework.stereotype.Service;

@Service
class LogRetrieverFactory {

  private final RiskRetriever riskRetriever;
  private final IssueRetriever issueRetriever;
  private final CombinedLogRetriever combinedLogRetriever;

  LogRetrieverFactory(
      RiskRetriever riskRetriever,
      IssueRetriever issueRetriever,
      CombinedLogRetriever combinedLogRetriever) {
    this.riskRetriever = riskRetriever;
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
          return riskRetriever;
        case ISSUE:
          return issueRetriever;
      }
    }

    return combinedLogRetriever;
  }
}
