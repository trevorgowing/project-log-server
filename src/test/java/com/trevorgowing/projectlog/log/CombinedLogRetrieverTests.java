package com.trevorgowing.projectlog.log;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO;
import com.trevorgowing.projectlog.log.issue.Issue;
import com.trevorgowing.projectlog.log.issue.IssueDTOFactory;
import com.trevorgowing.projectlog.log.issue.IssueRetriever;
import com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO;
import com.trevorgowing.projectlog.log.risk.Risk;
import com.trevorgowing.projectlog.log.risk.RiskDTOFactory;
import com.trevorgowing.projectlog.log.risk.RiskRetriever;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CombinedLogRetrieverTests extends AbstractTests {

  @Mock private LogRepository logRepository;
  @Mock private IssueRetriever issueRetriever;
  @Mock private RiskDTOFactory riskDTOFactory;
  @Mock private RiskRetriever riskRetriever;
  @Mock private IssueDTOFactory issueDTOFactory;

  @InjectMocks private CombinedLogRetriever combinedLogRetriever;

  @Test
  public void testGetLogDTOs_shouldDelegateRiskAndIssueCRUDServicesAndReturnUnionOfLogs() {
    // Set up fixture
    List<LogDTO> riskDTOs =
        asList(
            anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
            anIdentifiedRiskDTO().id(2).summary("Risk Two").build());
    List<LogDTO> issueDTOs =
        asList(
            anIdentifiedIssueDTO().id(1).summary("Issue One").build(),
            anIdentifiedIssueDTO().id(2).summary("Issue Two").build());

    List<LogDTO> expectedLogDTOs = new ArrayList<>();
    expectedLogDTOs.addAll(riskDTOs);
    expectedLogDTOs.addAll(issueDTOs);

    // Set up expectations
    when(riskRetriever.getLogDTOs()).thenReturn(riskDTOs);
    when(issueRetriever.getLogDTOs()).thenReturn(issueDTOs);

    // Exercise SUT
    List<LogDTO> actualLogDTOs = combinedLogRetriever.getLogDTOs();

    // Verify behaviour
    assertThat(actualLogDTOs, is(expectedLogDTOs));
  }

  @Test(expected = LogNotFoundException.class)
  public void testGetLogDTOByIdWithNoMatchingLog_shouldThrowLogNotFoundException() {
    // Set up expectations
    when(logRepository.findOne(1L)).thenReturn(null);

    // Exercise SUT
    combinedLogRetriever.getLogDTOById(1);
  }

  @Test
  public void
      testGetLogDTOByIdWithRisk_shouldDelegateToLogRepositoryAndRiskDTOFactoryAndReturnIdentifiedRiskDTO() {
    // Set up expectations
    Risk risk = aRisk().id(1).build();

    IdentifiedRiskDTO expectedLogDTO = anIdentifiedRiskDTO().id(1).build();

    // Set up expectations
    when(logRepository.findOne(1L)).thenReturn(risk);
    when(riskDTOFactory.createIdentifiedRiskDTO(risk)).thenReturn(expectedLogDTO);

    // Exercise SUT
    LogDTO actualLogDTO = combinedLogRetriever.getLogDTOById(1);

    // Verify behaviour
    assertThat(actualLogDTO, is(expectedLogDTO));
  }

  @Test
  public void
      testGetLogDTOByIdWithIssue_shouldDelegateToLogRepositoryAndIssueDTOFactoryAndReturnIdentifiedIssueDTO() {
    // Set up fixture
    Issue issue = anIssue().id(1).build();

    IdentifiedIssueDTO expectedLogDTO = anIdentifiedIssueDTO().id(1).build();

    // Set up expectations
    when(logRepository.findOne(1L)).thenReturn(issue);
    when(issueDTOFactory.createIdentifiedIssueDTO(issue)).thenReturn(expectedLogDTO);

    // Exercise SUT
    LogDTO actualLogDTO = combinedLogRetriever.getLogDTOById(1);

    // Verify behaviour
    assertThat(actualLogDTO, is(expectedLogDTO));
  }
}
