package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.project.ProjectRepository;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import com.trevorgowing.projectlog.user.UserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class RiskCRUDServiceTests extends AbstractTests {

    @Mock
    private RiskRepository riskRepository;

    @InjectMocks
    private RiskCRUDService riskCRUDService;

    @Test
    public void testGetLogDTOs_shouldDelegateToRiskRepositoryAndReturnLogDTOs() {
        // Set up fixture
        List<IdentifiedRiskDTO> identifiedRiskDTOs = asList(
                anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
                anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

        List<LogDTO> expectedLogDTOs = new ArrayList<>(identifiedRiskDTOs);

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(identifiedRiskDTOs);

        // Exercise SUT
        List<LogDTO> actualLogDTOs = riskCRUDService.getLogDTOs();

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }

    @Test(expected = RiskNotFoundException.class)
    public void testGetLogDTOByIdWithNoMatchingRisk_shouldThrowIdentifiedRiskNotFoundException() {
        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOById(1L)).thenReturn(null);

        // Exercise SUT
        riskCRUDService.getLogDTOById(1L);
    }

    @Test
    public void testGetLogDTOByIdWithMatchingRisk_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTO() {
        // Set up fixture
        IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOById(1L)).thenReturn(expectedIdentifiedRiskDTO);

        // Exercise SUT
        LogDTO actualLogDTO = riskCRUDService.getLogDTOById(1L);

        // Verify behaviour
        assertThat(actualLogDTO, is(expectedIdentifiedRiskDTO));
    }

    @Test
    public void testGetIdentifiedRiskDTOs_shouldDelegateToRiskRepositoryAndReturnIdentifiedRiskDTOs() {
        // Set up fixture
        List<IdentifiedRiskDTO> expectedIdentifiedRiskDTOs = asList(
                anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
                anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(expectedIdentifiedRiskDTOs);

        // Exercise SUT
        List<IdentifiedRiskDTO> actualIdentifiedRiskDTOs = riskCRUDService.getIdentifiedRiskDTOs();

        // Verify behaviour
        assertThat(actualIdentifiedRiskDTOs, is(expectedIdentifiedRiskDTOs));
    }
}