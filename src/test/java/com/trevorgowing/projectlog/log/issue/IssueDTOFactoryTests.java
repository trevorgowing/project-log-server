package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.ProjectDTOFactory;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.UserDTOFactory;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.aCompleteIssue;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class IssueDTOFactoryTests extends AbstractTests {

    @Mock
    private UserDTOFactory userDTOFactory;
    @Mock
    private ProjectDTOFactory projectDTOFactory;

    @InjectMocks
    private IssueDTOFactory issueDTOFactory;

    @Test
    public void testCreateIdentifiedIssueDTO_shouldCreateIdentifiedRiskDTOWithValuesFromGivenRisk() {
        // Set up fixtures
        Issue issue = aCompleteIssue().project(aProject().id(1L).build()).owner(aUser().id(1L).build()).build();

        IdentifiedUserDTO identifiedUserDTO = anIdentifiedUserDTO().id(1L).build();
        IdentifiedProjectDTO identifiedProjectDTO = anIdentifiedProjectDTO().id(1L).build();

        IdentifiedIssueDTO expectedIdentifiedIssueDTO = anIdentifiedIssueDTO()
                .id(issue.getId())
                .summary(issue.getSummary())
                .description(issue.getDescription())
                .category(issue.getCategory())
                .impact(issue.getImpact())
                .status(issue.getStatus())
                .dateClosed(issue.getDateClosed())
                .project(identifiedProjectDTO)
                .owner(identifiedUserDTO)
                .build();

        // Set up expectations
        when(userDTOFactory.createIdentifiedUserDTO(issue.getOwner())).thenReturn(identifiedUserDTO);
        when(projectDTOFactory.createIdentifiedProjectDTO(issue.getProject())).thenReturn(identifiedProjectDTO);

        // Exercise SUT
        IdentifiedIssueDTO actualIdentifiedIssueDTO = issueDTOFactory.createIdentifiedIssueDTO(issue);

        // Verify behaviour
        assertThat(actualIdentifiedIssueDTO, sameBeanAs(expectedIdentifiedIssueDTO));
    }
}
