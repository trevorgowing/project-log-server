package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO;
import com.trevorgowing.projectlog.log.issue.IssueCRUDService;
import com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class LogCRUDServiceTests extends AbstractTests {

    @Mock
    private RiskCRUDService riskCRUDService;
    @Mock
    private IssueCRUDService issueCRUDService;

    @InjectMocks
    private LogCRUDService logCRUDService;

    @Test
    public void testGetLogDTOs_shouldDelegateRiskAndIssueCRUDServicesAndReturnUnionOfLogs() {
        // Set up fixture
        List<IdentifiedRiskDTO> riskDTOs = asList(anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
                anIdentifiedRiskDTO().id(2).summary("Risk Two").build());
        List<IdentifiedIssueDTO> issueDTOs = asList(anIdentifiedIssueDTO().id(1).summary("Issue One").build(),
                anIdentifiedIssueDTO().id(2).summary("Issue Two").build());

        List<LogDTO> expectedLogDTOs = new ArrayList<>();
        expectedLogDTOs.addAll(riskDTOs);
        expectedLogDTOs.addAll(issueDTOs);

        // Set up expectations
        when(riskCRUDService.getIdentifiedRiskDTOs()).thenReturn(riskDTOs);
        when(issueCRUDService.getIdentifiedIssueDTOs()).thenReturn(issueDTOs);

        // Exercise SUT
        List<LogDTO> actualLogDTOs = logCRUDService.getLogDTOs();

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }
}