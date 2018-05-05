package com.trevorgowing.projectlog.log.issue;

class IssueNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6763411097730528550L;

  static final String REASON = "Issue not found for id";

  private IssueNotFoundException(long issueId) {
    super(REASON + ": " + issueId);
  }

  static IssueNotFoundException identifiedIssueNotFoundException(long issueId) {
    return new IssueNotFoundException(issueId);
  }
}
