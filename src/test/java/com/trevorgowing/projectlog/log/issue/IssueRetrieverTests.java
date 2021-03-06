package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.LogDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class IssueRetrieverTests extends AbstractTests {

  @Mock private IssueRepository issueRepository;

  @InjectMocks private IssueRetriever issueRetriever;

  @Test(expected = IssueNotFoundException.class)
  public void testFindIssueWithNonExistentIssue_shouldThrowIssueNotFoundException() {
    // Set up expectations
    when(issueRepository.findById(1L)).thenReturn(Optional.empty());

    // Exercise SUT
    issueRetriever.findIssue(1L);
  }

  @Test
  public void testFindIssueWithExistingIssue_shouldReturnIssue() {
    // Set up fixture
    Issue expectedIssue = anIssue().id(1L).build();

    // Set up expectations
    when(issueRepository.findById(1L)).thenReturn(Optional.of(expectedIssue));

    // Exercise SUT
    Issue actualIssue = issueRetriever.findIssue(1L);

    // Verify behaviour
    assertThat(actualIssue, is(expectedIssue));
  }

  @Test
  public void testGetLogDTOs_shouldDelegateToIssueRepositoryAndReturnLogDTOs() {
    // Set up fixture
    List<IdentifiedIssueDTO> identifiedIssueDTOs =
        asList(
            anIdentifiedIssueDTO().id(1).summary("Issue One").build(),
            anIdentifiedIssueDTO().id(2).summary("Issue Two").build());

    List<LogDTO> expectedLogDTOs = new ArrayList<>(identifiedIssueDTOs);

    // Set up expectations
    when(issueRepository.findIdentifiedIssueDTOs()).thenReturn(identifiedIssueDTOs);

    // Exercise SUT
    List<LogDTO> actualLogDTOs = issueRetriever.getLogDTOs();

    // Verify behaviour
    assertThat(actualLogDTOs, is(expectedLogDTOs));
  }

  @Test(expected = IssueNotFoundException.class)
  public void testGetLogDTOByIdWithNoMatchingIssue_shouldThrowIssueNotFoundException() {
    // Set up expectations
    when(issueRepository.findIdentifiedIssueDTOById(1L)).thenReturn(null);

    // Exercise SUT
    issueRetriever.getLogDTOById(1L);
  }

  @Test
  public void
      testGetLogDTOByIdWithMatchingIssue_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTO() {
    // Set up fixture
    IdentifiedIssueDTO expectedIdentifiedIssueDTO = anIdentifiedIssueDTO().id(1L).build();

    // Set up expectations
    when(issueRepository.findIdentifiedIssueDTOById(1L)).thenReturn(expectedIdentifiedIssueDTO);

    // Exercise SUT
    LogDTO actualLogDTO = issueRetriever.getLogDTOById(1L);

    // Verify behaviour
    assertThat(actualLogDTO, is(expectedIdentifiedIssueDTO));
  }

  @Test
  public void
      testGetIdentifiedIssueDTOs_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTOs() {
    // Set up fixture
    List<IdentifiedIssueDTO> expectedIdentifiedIssueDTOs =
        asList(
            anIdentifiedIssueDTO().id(1).summary("Issue One").build(),
            anIdentifiedIssueDTO().id(2).summary("Issue Two").build());

    // Set up expectations
    when(issueRepository.findIdentifiedIssueDTOs()).thenReturn(expectedIdentifiedIssueDTOs);

    // Exercise SUT
    List<IdentifiedIssueDTO> actualIdentifiedIssueDTOs = issueRetriever.getIdentifiedIssueDTOs();

    // Verify behaviour
    assertThat(actualIdentifiedIssueDTOs, is(expectedIdentifiedIssueDTOs));
  }
}
