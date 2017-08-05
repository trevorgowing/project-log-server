package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.issue.IssueRetriever;
import com.trevorgowing.projectlog.log.risk.RiskCRUDService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.trevorgowing.projectlog.log.constant.LogType.ISSUE;
import static com.trevorgowing.projectlog.log.constant.LogType.RISK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("unchecked")
public class LogRetrieverFactoryTests extends AbstractTests {

    @Mock
    private RiskCRUDService riskCRUDService;
    @Mock
    private IssueRetriever issueRetriever;
    @Mock
    private CombinedLogRetriever combinedLogRetriever;

    @InjectMocks
    private LogRetrieverFactory logRetrieverFactory;

    @Test
    public void testGetLogCRUDService_shouldReturnCombinedLogCRUDService() {
        // Exercise SUT
        LogRetriever actualLogRetriever = logRetrieverFactory.getLogLookupService();

        // Verify behaviour
        assertThat(actualLogRetriever, is(combinedLogRetriever));
    }

    @Test
    public void testGetLogCRUDServiceWithTypeRisk_shouldReturnRiskCRUDService() {
        // Exercise SUT
        LogRetriever actualLogRetriever = logRetrieverFactory.getLogLookupService(RISK);

        // Verify behaviour
        assertThat(actualLogRetriever, is(riskCRUDService));
    }

    @Test
    public void testGetLogCRUDServiceWithTypeIssue_shouldReturnIssueCRUDService() {
        // Exercise SUT
        LogRetriever actualLogRetriever = logRetrieverFactory.getLogLookupService(ISSUE);

        // Verify behaviour
        assertThat(actualLogRetriever, is(issueRetriever));
    }
}