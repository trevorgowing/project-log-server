package com.trevorgowing.projectlog.project;

class ProjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6242411532425616601L;

    static final String REASON = "Project not found for id";

    private ProjectNotFoundException(long projectId) {
        super(REASON + ": " + projectId);
    }

    static ProjectNotFoundException identifiedProjectNotFoundException(long projectId) {
        return new ProjectNotFoundException(projectId);
    }
}