package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectCRUDService;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserCRUDService;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static com.trevorgowing.projectlog.log.issue.UnidentifiedIssueDTOBuilder.anUnidentifiedIssueDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class IssueCRUDServiceTests extends AbstractTests {

    @Mock
    private IssueRepository issueRepository;
    @Mock
    private UserCRUDService userCRUDService;
    @Mock
    private ProjectCRUDService projectCRUDService;

    @InjectMocks
    private IssueCRUDService issueCRUDService;

    @Test
    public void testGetLogDTOs_shouldDelegateToIssueRepositoryAndReturnLogDTOs() {
        // Set up fixture
        List<IdentifiedIssueDTO> identifiedIssueDTOs = asList(
                anIdentifiedIssueDTO().id(1).summary("Issue One").build(),
                anIdentifiedIssueDTO().id(2).summary("Issue Two").build());

        List<LogDTO> expectedLogDTOs = new ArrayList<>(identifiedIssueDTOs);

        // Set up expectations
        when(issueRepository.findIdentifiedIssueDTOs()).thenReturn(identifiedIssueDTOs);

        // Exercise SUT
        List<LogDTO> actualLogDTOs = issueCRUDService.getLogDTOs();

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }

    @Test(expected = IssueNotFoundException.class)
    public void testGetLogDTOByIdWithNoMatchingIssue_shouldThrowIssueNotFoundException() {
        // Set up expectations
        when(issueRepository.findIdentifiedIssueDTOById(1L)).thenReturn(null);

        // Exercise SUT
        issueCRUDService.getLogDTOById(1L);
    }

    @Test
    public void testGetLogDTOByIdWithMatchingIssue_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTO() {
        // Set up fixture
        IdentifiedIssueDTO expectedIdentifiedIssueDTO = anIdentifiedIssueDTO().id(1L).build();

        // Set up expectations
        when(issueRepository.findIdentifiedIssueDTOById(1L)).thenReturn(expectedIdentifiedIssueDTO);

        // Exercise SUT
        LogDTO actualLogDTO = issueCRUDService.getLogDTOById(1L);

        // Verify behaviour
        assertThat(actualLogDTO, is(expectedIdentifiedIssueDTO));
    }

    @Test
    public void testGetIdentifiedIssueDTOs_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTOs() {
        // Set up fixture
        List<IdentifiedIssueDTO> expectedIdentifiedIssueDTOs = asList(
                anIdentifiedIssueDTO().id(1).summary("Issue One").build(),
                anIdentifiedIssueDTO().id(2).summary("Issue Two").build());

        // Set up expectations
        when(issueRepository.findIdentifiedIssueDTOs()).thenReturn(expectedIdentifiedIssueDTOs);

        // Exercise SUT
        List<IdentifiedIssueDTO> actualIdentifiedIssueDTOs = issueCRUDService.getIdentifiedIssueDTOs();

        // Verify behaviour
        assertThat(actualIdentifiedIssueDTOs, is(expectedIdentifiedIssueDTOs));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testCreateIssueWithNonExistentProject_shouldThrowProjectNotFoundException() {
        // Set up fixture
        UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO()
                .project(anIdentifiedProjectDTO().id(1L).build())
                .build();

        // Set up expectations
        when(projectCRUDService.findProject(1L)).thenThrow(identifiedProjectNotFoundException(1L));

        // Exercise SUT
        issueCRUDService.createIssue(unidentifiedIssueDTO);
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
        when(projectCRUDService.findProject(1L)).thenReturn(project);
        when(userCRUDService.findUser(1L)).thenThrow(identifiedUserNotFoundException(1L));

        // Exercise SUT
        issueCRUDService.createIssue(unidentifiedIssueDTO);
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
        when(projectCRUDService.findProject(1L)).thenReturn(project);
        when(userCRUDService.findUser(1L)).thenReturn(owner);
        when(issueRepository.save(argThat(samePropertyValuesAs(unidentifiedIssue)))).thenReturn(expectedIssue);

        // Exercise SUT
        Issue actualIssue = issueCRUDService.createIssue(unidentifiedIssueDTO);

        // Verify behaviour
        assertThat(actualIssue, is(expectedIssue));
    }
}