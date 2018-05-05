package com.trevorgowing.projectlog.log.risk;

class RiskNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6522619458873139805L;

  static final String REASON = "Risk not found for id";

  private RiskNotFoundException(long riskId) {
    super(REASON + ": " + riskId);
  }

  static RiskNotFoundException identifiedRiskNotFoundException(long riskId) {
    return new RiskNotFoundException(riskId);
  }
}
