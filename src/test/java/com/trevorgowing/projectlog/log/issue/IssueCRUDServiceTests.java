package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class IssueCRUDServiceTests extends AbstractTests {

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueCRUDService issueCRUDService;

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
}