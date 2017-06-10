package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.issue.IssueCRUDService;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.trevorgowing.projectlog.log.constant.LogType.ISSUE;
import static com.trevorgowing.projectlog.log.constant.LogType.RISK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("unchecked")
public class LogCRUDServiceFactoryTests extends AbstractTests {

    @Mock
    private RiskCRUDService riskCRUDService;
    @Mock
    private IssueCRUDService issueCRUDService;
    @Mock
    private CombinedLogCRUDService combinedLogCRUDService;

    @InjectMocks
    private LogCRUDServiceFactory logCRUDServiceFactory;

    @Test
    public void testGetLogCRUDService_shouldReturnCombinedLogCRUDService() {
        // Exercise SUT
        LogCRUDService actualLogCRUDService = logCRUDServiceFactory.getLogCRUDService();

        // Verify behaviour
        assertThat(actualLogCRUDService, is(combinedLogCRUDService));
    }

    @Test
    public void testGetLogCRUDServiceWithTypeRisk_shouldReturnRiskCRUDService() {
        // Exercise SUT
        LogCRUDService actualLogCRUDService = logCRUDServiceFactory.getLogCRUDService(RISK);

        // Verify behaviour
        assertThat(actualLogCRUDService, is(riskCRUDService));
    }

    @Test
    public void testGetLogCRUDServiceWithTypeIssue_shouldReturnIssueCRUDService() {
        // Exercise SUT
        LogCRUDService actualLogCRUDService = logCRUDServiceFactory.getLogCRUDService(ISSUE);

        // Verify behaviour
        assertThat(actualLogCRUDService, is(issueCRUDService));
    }
}