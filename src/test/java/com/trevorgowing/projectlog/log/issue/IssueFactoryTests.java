package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static com.trevorgowing.projectlog.log.issue.UnidentifiedIssueDTOBuilder.anUnidentifiedIssueDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class IssueFactoryTests extends AbstractTests {

	@Mock
	private UserRetriever userRetriever;
	@Mock
	private ProjectRetriever projectRetriever;
	@Mock
	private IssueRepository issueRepository;

	@InjectMocks
	private IssueFactory issueFactory;

	@Test(expected = ProjectNotFoundException.class)
	public void testCreateIssueWithNonExistentProject_shouldThrowProjectNotFoundException() {
		// Set up fixture
		UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO()
				.project(anIdentifiedProjectDTO().id(1L).build())
				.build();

		// Set up expectations
		when(projectRetriever.findProject(1L)).thenThrow(identifiedProjectNotFoundException(1L));

		// Exercise SUT
		issueFactory.createIssue(unidentifiedIssueDTO);
	}

	@Test(expected = UserNotFoundException.class)
	public void testCreateIssueWithNonExistentOwner_shouldThrowUserNotFoundException() {
		// Set up fixture
		UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO()
				.project(anIdentifiedProjectDTO().id(1L).build())
				.owner(anIdentifiedUserDTO().id(1L).build())
				.build();

		Project project = aProject().build();

		// Set up expectations
		when(projectRetriever.findProject(1L)).thenReturn(project);
		when(userRetriever.findUser(1L)).thenThrow(identifiedUserNotFoundException(1L));

		// Exercise SUT
		issueFactory.createIssue(unidentifiedIssueDTO);
	}

	@Test
	public void testCreateIssueWithValidIssue_shouldDelegateToIssueRepositoryToSaveIssueAndReturnIssue() {
		// Set up fixture
		UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO()
				.summary("Summary")
				.description("Description")
				.category(Category.COMMITTED_PEOPLE)
				.impact(Impact.MODERATE)
				.status(LogStatus.NEW)
				.dateClosed(null)
				.project(anIdentifiedProjectDTO().id(1L).build())
				.owner(anIdentifiedUserDTO().id(1L).build())
				.build();

		Project project = aProject().id(1L).build();
		User owner = aUser().id(1L).build();

		Issue unidentifiedIssue = anIssue()
				.summary("Summary")
				.description("Description")
				.category(Category.COMMITTED_PEOPLE)
				.impact(Impact.MODERATE)
				.status(LogStatus.NEW)
				.project(project)
				.dateClosed(null)
				.owner(owner)
				.build();

		Issue expectedIssue = anIssue()
				.id(1L)
				.summary("Summary")
				.description("Description")
				.category(Category.COMMITTED_PEOPLE)
				.impact(Impact.MODERATE)
				.status(LogStatus.NEW)
				.dateClosed(null)
				.project(project)
				.owner(owner)
				.build();

		// Set up expectations
		when(projectRetriever.findProject(1L)).thenReturn(project);
		when(userRetriever.findUser(1L)).thenReturn(owner);
		when(issueRepository.save(argThat(samePropertyValuesAs(unidentifiedIssue)))).thenReturn(expectedIssue);

		// Exercise SUT
		Issue actualIssue = issueFactory.createIssue(unidentifiedIssueDTO);

		// Verify behaviour
		assertThat(actualIssue, is(expectedIssue));
	}

}
