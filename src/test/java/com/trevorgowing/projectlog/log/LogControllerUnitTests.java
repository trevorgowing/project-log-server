package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO;
import com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class LogControllerUnitTests extends AbstractControllerUnitTests {

    @Mock
    private LogCRUDService logCRUDService;
    @InjectMocks
    private LogController logController;

    @Override
    protected Object getController() {
        return logController;
    }

    @Test
    public void testGetAllLogsWithNoExistingLogs_shouldRespondWithStatusOkAndReturnNoLogs() throws Exception {
        // Set up expectations
        when(logCRUDService.getLogDTOs()).thenReturn(Collections.emptyList());

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(LogConstants.LOGS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(Collections.<LogDTO>emptyList())));

        List<LogDTO> actualUnidentifiedIssueDTOS = logController.getLogs();

        // Verify behaviour
        assertThat(actualUnidentifiedIssueDTOS, is(empty()));
    }

    @Test
    public void testGetAllLogsWithExistingLogs_shouldRespondWithStatusOkAndReturnAllLogs() throws Exception {
        // Set up expectations
        IdentifiedIssueDTO identifiedIssueDTO = anIdentifiedIssueDTO().build();
        IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().build();

        List<LogDTO> expectedLogDTOs = asList(identifiedIssueDTO, identifiedRiskDTO);

        when(logCRUDService.getLogDTOs()).thenReturn(expectedLogDTOs);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(LogConstants.LOGS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedLogDTOs)));

        List<LogDTO> actualLogDTOs = logController.getLogs();

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }
}
