package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.log.issue.Issue.unidentifiedIssue;

@Service
public class IssueFactory {

	private final UserRetriever userRetriever;
	private final ProjectRetriever projectRetriever;
	private final IssueRepository issueRepository;

	public IssueFactory(UserRetriever userRetriever, ProjectRetriever projectRetriever,
						IssueRepository issueRepository) {
		this.userRetriever = userRetriever;
		this.projectRetriever = projectRetriever;
		this.issueRepository = issueRepository;
	}

	public Issue createIssue(UnidentifiedIssueDTO unidentifiedIssueDTO) {
		Project project = projectRetriever.findProject(unidentifiedIssueDTO.getProject().getId());
		User owner = userRetriever.findUser(unidentifiedIssueDTO.getOwner().getId());

		Issue issue = unidentifiedIssue(unidentifiedIssueDTO.getSummary(), unidentifiedIssueDTO.getDescription(),
				unidentifiedIssueDTO.getCategory(), unidentifiedIssueDTO.getImpact(), unidentifiedIssueDTO.getStatus(),
				unidentifiedIssueDTO.getDateClosed(), project, owner);

		return issueRepository.save(issue);
	}

}
