package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.LogDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RiskRetrieverTests extends AbstractTests {

  @Mock private RiskRepository riskRepository;

  @InjectMocks private RiskRetriever riskRetriever;

  @Test(expected = RiskNotFoundException.class)
  public void testFindRiskWithNonExistentRisk_shouldThrowRiskNotFoundException() {
    // Set up expectations
    when(riskRepository.findOne(1L)).thenThrow(identifiedRiskNotFoundException(1L));

    // Exercise SUT
    riskRetriever.findRisk(1L);
  }

  @Test
  public void testFindRiskWithExistingRisk_shouldReturnRisk() {
    // Set up fixture
    Risk expectedRisk = aRisk().id(1L).build();

    // Set up expectations
    when(riskRepository.findOne(1L)).thenReturn(expectedRisk);

    // Exercise SUT
    Risk actualRisk = riskRetriever.findRisk(1L);

    // Verify behaviour
    assertThat(actualRisk, is(expectedRisk));
  }

  @Test
  public void testGetLogDTOs_shouldDelegateToRiskRepositoryAndReturnLogDTOs() {
    // Set up fixture
    List<IdentifiedRiskDTO> identifiedRiskDTOs =
        asList(
            anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
            anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

    List<LogDTO> expectedLogDTOs = new ArrayList<>(identifiedRiskDTOs);

    // Set up expectations
    when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(identifiedRiskDTOs);

    // Exercise SUT
    List<LogDTO> actualLogDTOs = riskRetriever.getLogDTOs();

    // Verify behaviour
    assertThat(actualLogDTOs, is(expectedLogDTOs));
  }

  @Test(expected = RiskNotFoundException.class)
  public void testGetLogDTOByIdWithNoMatchingRisk_shouldThrowIdentifiedRiskNotFoundException() {
    // Set up expectations
    when(riskRepository.findIdentifiedRiskDTOById(1L)).thenReturn(null);

    // Exercise SUT
    riskRetriever.getLogDTOById(1L);
  }

  @Test
  public void
      testGetLogDTOByIdWithMatchingRisk_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTO() {
    // Set up fixture
    IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    // Set up expectations
    when(riskRepository.findIdentifiedRiskDTOById(1L)).thenReturn(expectedIdentifiedRiskDTO);

    // Exercise SUT
    LogDTO actualLogDTO = riskRetriever.getLogDTOById(1L);

    // Verify behaviour
    assertThat(actualLogDTO, is(expectedIdentifiedRiskDTO));
  }

  @Test
  public void
      testGetIdentifiedRiskDTOs_shouldDelegateToRiskRepositoryAndReturnIdentifiedRiskDTOs() {
    // Set up fixture
    List<IdentifiedRiskDTO> expectedIdentifiedRiskDTOs =
        asList(
            anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
            anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

    // Set up expectations
    when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(expectedIdentifiedRiskDTOs);

    // Exercise SUT
    List<IdentifiedRiskDTO> actualIdentifiedRiskDTOs = riskRetriever.getIdentifiedRiskDTOs();

    // Verify behaviour
    assertThat(actualIdentifiedRiskDTOs, is(expectedIdentifiedRiskDTOs));
  }
}
