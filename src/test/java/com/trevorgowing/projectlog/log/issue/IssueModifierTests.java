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

import java.time.LocalDate;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
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

public class IssueModifierTests extends AbstractTests {

    @Mock
    private UserRetriever userModifier;
    @Mock
    private IssueRetriever issueRetriever;
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private ProjectRetriever projectRetriever;

    @InjectMocks
    private IssueModifier issueModifier;

    @Test(expected = ProjectNotFoundException.class)
    public void testUpdateIssueWithNonExistentProject_shouldThrowProjectNotFoundException() {
        // Set up fixture
        IdentifiedIssueDTO identifiedIssueDTO = anIdentifiedIssueDTO()
                .id(1L)
                .project(anIdentifiedProjectDTO().id(2L).build())
                .build();

        Issue issue = anIssue()
                .id(1L)
                .project(aProject().id(1L).build())
                .build();

        // Set up expectations
        when(issueRetriever.findIssue(1L)).thenReturn(issue);
        when(projectRetriever.findProject(2L)).thenThrow(identifiedProjectNotFoundException(2L));

        // Exercise SUT
        issueModifier.updateIssue(identifiedIssueDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateIssueWithNonExistentOwner_shouldThrowUserNotFoundException() {
        // Set up fixture
        IdentifiedIssueDTO identifiedIssueDTO = anIdentifiedIssueDTO()
                .id(1L)
                .owner(anIdentifiedUserDTO().id(2L).build())
                .build();

        Issue issue = anIssue()
                .id(1L)
                .owner(aUser().id(1L).build())
                .build();

        // Set up expectations
        when(issueRetriever.findIssue(1L)).thenReturn(issue);
        when(userModifier.findUser(2L)).thenThrow(identifiedUserNotFoundException(2L));

        // Exercise SUT
        issueModifier.updateIssue(identifiedIssueDTO);
    }

    @Test
    public void testUpdateIssueWithValidIssue_shouldDelegateToIssueRepositoryToSaveIssueAndReturnUpdateIssue() {
        // Set up expectations
        IdentifiedIssueDTO identifiedIssueDTO = anIdentifiedIssueDTO()
                .id(1L)
                .summary("New Summary")
                .description("New Description")
                .category(Category.COMMITTED_PEOPLE)
                .impact(Impact.INSIGNIFICANT)
                .status(LogStatus.CLOSED)
                .dateClosed(LocalDate.MAX)
                .project(anIdentifiedProjectDTO().id(2L).build())
                .owner(anIdentifiedUserDTO().id(2L).build())
                .build();

        Issue issuePreUpdate = anIssue()
                .id(1L)
                .summary("Old Summary")
                .description("Old Description")
                .category(Category.QUALITY_EXECUTION)
                .impact(Impact.MODERATE)
                .status(LogStatus.NEW)
                .dateClosed(null)
                .project(aProject().id(1L).build())
                .owner(aUser().id(1L).build())
                .build();

        Project project = aProject().id(2L).build();
        User owner = aUser().id(2L).build();

        Issue expectedIssue = anIssue()
                .id(1L)
                .summary("New Summary")
                .description("New Description")
                .category(Category.COMMITTED_PEOPLE)
                .impact(Impact.INSIGNIFICANT)
                .status(LogStatus.CLOSED)
                .dateClosed(LocalDate.MAX)
                .project(project)
                .owner(owner)
                .build();

        // Set up expectations
        when(issueRetriever.findIssue(1L)).thenReturn(issuePreUpdate);
        when(projectRetriever.findProject(2L)).thenReturn(project);
        when(userModifier.findUser(2L)).thenReturn(owner);
        when(issueRepository.save(argThat(samePropertyValuesAs(expectedIssue)))).thenReturn(expectedIssue);

        // Exercise SUT
        Issue actualIssue = issueModifier.updateIssue(identifiedIssueDTO);

        // Verify behaviour
        assertThat(actualIssue, is(expectedIssue));
    }
}