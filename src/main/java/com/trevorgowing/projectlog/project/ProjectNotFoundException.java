package com.trevorgowing.projectlog.project;

public class ProjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6242411532425616601L;

    public static final String REASON = "Project not found for id";

    private ProjectNotFoundException(long projectId) {
        super(REASON + ": " + projectId);
    }

    public static ProjectNotFoundException identifiedProjectNotFoundException(long projectId) {
        return new ProjectNotFoundException(projectId);
    }
}