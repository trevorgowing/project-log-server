package com.trevorgowing.projectlog.log;

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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.log.constant.LogType;
import com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO;
import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

public class LogControllerUnitTests extends AbstractControllerUnitTests {

  @Mock private LogRetriever logRetriever;

  @Mock private LogDeleter logDeleter;
  @Mock private LogRetrieverFactory logRetrieverFactory;

  @InjectMocks private LogController logController;

  @Override
  protected Object getController() {
    return logController;
  }

  @Test
  public void testGetAllLogsWithNoExistingLogs_shouldRespondWithStatusOkAndReturnNoLogs() {
    // Set up expectations
    when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
    when(logRetriever.getLogDTOs()).thenReturn(emptyList());

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(LogConstants.LOGS_URL_PATH)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(Collections.<LogDTO>emptyList())));

    List<LogDTO> actualUnidentifiedIssueDTOS = logController.getLogs();

    // Verify behaviour
    assertThat(actualUnidentifiedIssueDTOS, is(empty()));
  }

  @Test
  public void testGetAllLogsWithExistingLogs_shouldRespondWithStatusOkAndReturnAllLogs() {
    // Set up fixture
    List<LogDTO> expectedLogDTOs =
        asList(anIdentifiedIssueDTO().build(), anIdentifiedRiskDTO().build());

    // Set up expectations
    when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
    when(logRetriever.getLogDTOs()).thenReturn(expectedLogDTOs);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(LogConstants.LOGS_URL_PATH)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedLogDTOs)));

    List<LogDTO> actualLogDTOs = logController.getLogs();

    // Verify behaviour
    assertThat(actualLogDTOs, is(expectedLogDTOs));
  }

  @Test(expected = LogTypeParsingException.class)
  public void testGetLogsWithInvalidType_shouldRespondWithBadRequest() {
    // Set up fixture
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("Unable to parse LogType, expected \"RISK\" or \"ISSUE\" but found: invalid")
            .build();

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, "invalid")
                .toString())
        .then()
        .log()
        .all()
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    logController.getLogs("invalid");
  }

  @Test
  public void
      testGetLogsWithValidTypeRiskAndNoExistingRisks_shouldRespondWithStatusOkAndReturnNoRisks() {
    // Set up expectations
    when(logRetrieverFactory.getLogLookupService(LogType.RISK)).thenReturn(logRetriever);
    when(logRetriever.getLogDTOs()).thenReturn(emptyList());

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.RISK.name())
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(Collections.<LogDTO>emptyList())));

    List<LogDTO> actualLogDTOs = logController.getLogs(LogType.RISK.name());

    // Verify behaviour
    assertThat(actualLogDTOs, is(empty()));
  }

  @Test
  public void
      testGetLogsWithValidTypeRiskAndExistingRisk_shouldRespondWithStatusOkAndReturnRisks() {
    // Set up fixture
    List<LogDTO> expectedLogDTOs =
        asList(anIdentifiedRiskDTO().build(), anIdentifiedRiskDTO().build());

    // Set up expectations
    when(logRetrieverFactory.getLogLookupService(LogType.RISK)).thenReturn(logRetriever);
    when(logRetriever.getLogDTOs()).thenReturn(expectedLogDTOs);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.RISK.name())
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedLogDTOs)));

    List<LogDTO> actualLogDTOs = logController.getLogs(LogType.RISK.name());

    // Verify behaviour
    assertThat(actualLogDTOs, is(expectedLogDTOs));
  }

  @Test
  public void
      testGetLogsWithValidTypeIssuesAndNoExistingIssue_shouldRespondWithStatusOkAndReturnNoIssues() {
    // Set up expectations
    when(logRetrieverFactory.getLogLookupService(LogType.ISSUE)).thenReturn(logRetriever);
    when(logRetriever.getLogDTOs()).thenReturn(emptyList());

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.ISSUE.name())
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(Collections.<LogDTO>emptyList())));

    List<LogDTO> actualLogDTOs = logController.getLogs(LogType.ISSUE.name());

    // Verify behaviour
    assertThat(actualLogDTOs, is(empty()));
  }

  @Test
  public void
      testGetLogsWithValidTypeIssueAndExistingIssues_shouldRespondWithStatusOkAndReturnIssues() {
    // Set up fixture
    List<LogDTO> expectedLogDTOs =
        asList(anIdentifiedIssueDTO().build(), anIdentifiedIssueDTO().build());

    // Set up expectations
    when(logRetrieverFactory.getLogLookupService(LogType.ISSUE)).thenReturn(logRetriever);
    when(logRetriever.getLogDTOs()).thenReturn(expectedLogDTOs);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendQuery(LogConstants.TYPE_QUERY_PARAMETER, LogType.ISSUE.name())
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedLogDTOs)));

    List<LogDTO> actualLogDTOs = logController.getLogs(LogType.ISSUE.name());

    // Verify behaviour
    assertThat(actualLogDTOs, is(expectedLogDTOs));
  }

  @Test(expected = LogNotFoundException.class)
  public void testGetLogDTOByIdWithNoMatchingLog_shouldRespondWithStatusNotFound() {
    // Set up fixture
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message("Log not found for id: 1").build();

    // Set up expectations
    when(logRetrieverFactory.getLogLookupService()).thenReturn(logRetriever);
    when(logRetriever.getLogDTOById(1)).thenThrow(identifiedLogNotFoundException(1));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(1).toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    logController.getLogById(1);
  }

  @Test
  public void testGetLogByIdWithMatchingLog_shouldRespondWithStatusOkAndReturnLog() {
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
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .body(is(convertToJSON(expectedLogDTO)));

    LogDTO actualLogDTO = logController.getLogById(1);

    // Verify behaviour
    assertThat(actualLogDTO, is(expectedLogDTO));
  }

  @Test(expected = LogNotFoundException.class)
  public void testDeleteLogByIdWithNonExistentLog_shouldRespondWithStatusNotFound() {
    // Set up fixture
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message("Log not found for id: 1").build();

    // Set up expectations
    doThrow(identifiedLogNotFoundException(1L)).when(logDeleter).deleteLogById(1L);

    // Exercise SUT
    given()
        .when()
        .delete(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(1L).toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    logController.deleteLogById(1L);
  }

  @Test
  public void testDeleteLogByIdWithExistingLog_shouldRespondWithStatusNoContent() {
    // Set up expectations
    doNothing().when(logDeleter).deleteLogById(1L);

    // Exercise SUT
    given()
        .when()
        .delete(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(1L).toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }
}
