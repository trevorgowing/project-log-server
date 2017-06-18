package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.log.constant.LogType;
import com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.trevorgowing.UrlStringBuilder.basedUrlBuilder;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.log.LogNotFoundException.identifiedLogNotFoundException;
import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class LogControllerUnitTests extends AbstractControllerUnitTests {

    @Mock
    private LogRetriever logRetriever;
    
    @Mock
    private LogRetrieverFactory logRetrieverFactory;

    @InjectMocks
    private LogController logController;

    @Override
    protected Object getController() {
        return logController;
    }

    @Test
    public void testGetAllLogsWithNoExistingLogs_shouldRespondWithStatusOkAndReturnNoLogs() throws Exception {
        // Set up expectations
        when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
        when(logRetriever.getLogDTOs()).thenReturn(emptyList());

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(LogConstants.LOGS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(Collections.<LogDTO>emptyList())));

        List<LogDTO> actualUnidentifiedIssueDTOS = logController.getLogs();

        // Verify behaviour
        assertThat(actualUnidentifiedIssueDTOS, is(empty()));
    }

    @Test
    public void testGetAllLogsWithExistingLogs_shouldRespondWithStatusOkAndReturnAllLogs() throws Exception {
        // Set up fixture
        List<LogDTO> expectedLogDTOs = asList(anIdentifiedIssueDTO().build(), anIdentifiedRiskDTO().build());

        // Set up expectations
        when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
        when(logRetriever.getLogDTOs()).thenReturn(expectedLogDTOs);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(LogConstants.LOGS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(expectedLogDTOs)));

        List<LogDTO> actualLogDTOs = logController.getLogs();

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }

    @Test(expected = LogTypeParsingException.class)
    public void testGetLogsWithInvalidType_shouldRespondWithBadRequest() {
        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                        .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, "invalid")
                        .toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        logController.getLogs("invalid");
    }

    @Test
    public void testGetLogsWithValidTypeRiskAndNoExistingRisks_shouldRespondWithStatusOkAndReturnNoRisks() throws Exception {
        // Set up expectations
        when(logRetrieverFactory.getLogLookupService(LogType.RISK)).thenReturn(logRetriever);
        when(logRetriever.getLogDTOs()).thenReturn(emptyList());

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                        .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.RISK.name())
                        .toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(Collections.<LogDTO>emptyList())));

        List<LogDTO> actualLogDTOs = logController.getLogs(LogType.RISK.name());

        // Verify behaviour
        assertThat(actualLogDTOs, is(empty()));
    }

    @Test
    public void testGetLogsWithValidTypeRiskAndExistingRisk_shouldRespondWithStatusOkAndReturnRisks() throws Exception {
        // Set up fixture
        List<LogDTO> expectedLogDTOs = asList(anIdentifiedRiskDTO().build(), anIdentifiedRiskDTO().build());

        // Set up expectations
        when(logRetrieverFactory.getLogLookupService(LogType.RISK)).thenReturn(logRetriever);
        when(logRetriever.getLogDTOs()).thenReturn(expectedLogDTOs);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                        .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.RISK.name())
                        .toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(expectedLogDTOs)));

        List<LogDTO> actualLogDTOs = logController.getLogs(LogType.RISK.name());

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }

    @Test
    public void testGetLogsWithValidTypeIssuesAndNoExistingIssue_shouldRespondWithStatusOkAndReturnNoIssues() throws Exception {
        // Set up expectations
        when(logRetrieverFactory.getLogLookupService(LogType.ISSUE)).thenReturn(logRetriever);
        when(logRetriever.getLogDTOs()).thenReturn(emptyList());

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                        .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.ISSUE.name())
                        .toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(Collections.<LogDTO>emptyList())));

        List<LogDTO> actualLogDTOs = logController.getLogs(LogType.ISSUE.name());

        // Verify behaviour
        assertThat(actualLogDTOs, is(empty()));
    }

    @Test
    public void testGetLogsWithValidTypeIssueAndExistingIssues_shouldRespondWithStatusOkAndReturnIssues() throws Exception {
        // Set up fixture
        List<LogDTO> expectedLogDTOs = asList(anIdentifiedIssueDTO().build(), anIdentifiedIssueDTO().build());

        // Set up expectations
        when(logRetrieverFactory.getLogLookupService(LogType.ISSUE)).thenReturn(logRetriever);
        when(logRetriever.getLogDTOs()).thenReturn(expectedLogDTOs);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                        .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.ISSUE.name())
                        .toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(expectedLogDTOs)));

        List<LogDTO> actualLogDTOs = logController.getLogs(LogType.ISSUE.name());

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }

    @Test(expected = LogNotFoundException.class)
    public void testGetLogDTOByIdWithNoMatchingLog_shouldRespondWithStatusNotFound() {
        // Set up expectations
        when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
        when(logRetriever.getLogDTOById(1)).thenThrow(identifiedLogNotFoundException(1));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(1).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());

        logController.getLogById(1);
    }

    @Test
    public void testGetLogByIdWithMatchingLog_shouldRespondWithStatusOkAndReturnLog() throws Exception {
        // Set up fixture
        IdentifiedRiskDTO expectedLogDTO = anIdentifiedRiskDTO().id(1).build();

        // Set up expectations
        when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
        when(logRetriever.getLogDTOById(1)).thenReturn(expectedLogDTO);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(1).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is(convertToJSON(expectedLogDTO)));

        LogDTO actualLogDTO = logController.getLogById(1);

        // Verify behaviour
        assertThat(actualLogDTO, is(expectedLogDTO));
    }
}
