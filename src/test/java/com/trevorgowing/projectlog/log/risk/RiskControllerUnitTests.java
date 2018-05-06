package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.UrlStringBuilder.basedUrlBuilder;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static com.trevorgowing.projectlog.log.risk.UnidentifiedRiskDTOBuilder.anUnidentifiedRiskDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

public class RiskControllerUnitTests extends AbstractControllerUnitTests {

  @Mock private RiskFactory riskFactory;
  @Mock private RiskModifier riskModifier;
  @Mock private RiskDTOFactory riskDTOFactory;

  @InjectMocks private RiskController riskController;

  @Override
  protected Object getController() {
    return riskController;
  }

  @Test(expected = ProjectNotFoundException.class)
  public void testPostRiskWithNonExistentProject_shouldRespondWithStatusConflict() {
    // Set up fixture
    UnidentifiedRiskDTO unidentifiedRiskDTO =
        anUnidentifiedRiskDTO().project(anIdentifiedProjectDTO().id(1L).build()).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("Project not found for id: 1").build();

    // Set up expectations
    when(riskFactory.createRisk(unidentifiedRiskDTO))
        .thenThrow(identifiedProjectNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedRiskDTO))
        .when()
        .post(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    riskController.postRisk(unidentifiedRiskDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testPostRiskWithNonExistentOwner_shouldRespondWithStatusConflict() {
    // Set up fixture
    UnidentifiedRiskDTO unidentifiedRiskDTO =
        anUnidentifiedRiskDTO().owner(anIdentifiedUserDTO().id(1L).build()).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("User not found for id: 1").build();

    // Set up expectations
    when(riskFactory.createRisk(unidentifiedRiskDTO))
        .thenThrow(identifiedUserNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedRiskDTO))
        .when()
        .post(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    riskController.postRisk(unidentifiedRiskDTO);
  }

  @Test
  public void testPostRiskWithValidRisk_shouldRespondWithStatusCreatedAndReturnIdentifiedRiskDTO() {
    // Set up fixture
    UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO().build();

    Risk risk = aRisk().build();

    IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO().build();

    // Set up expectations
    when(riskFactory.createRisk(unidentifiedRiskDTO)).thenReturn(risk);
    when(riskDTOFactory.createIdentifiedRiskDTO(risk)).thenReturn(expectedIdentifiedRiskDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedRiskDTO))
        .when()
        .post(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedRiskDTO)));

    IdentifiedRiskDTO actualIdentifiedRiskDTO = riskController.postRisk(unidentifiedRiskDTO);

    // Verify behaviour
    assertThat(actualIdentifiedRiskDTO, is(expectedIdentifiedRiskDTO));
  }

  @Test(expected = RiskNotFoundException.class)
  public void testPutRiskWithNonExistentRisk_shouldRespondWithStatusNotFound() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message("Risk not found for id: 1").build();

    // Set up expectations
    when(riskModifier.updateRisk(identifiedRiskDTO)).thenThrow(identifiedRiskNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedRiskDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    riskController.putRisk(identifiedRiskDTO);
  }

  @Test(expected = ProjectNotFoundException.class)
  public void testPutRiskWithNonExistentProject_shouldRespondWithStatusConflict() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("Project not found for id: 1").build();

    // Set up expectations
    when(riskModifier.updateRisk(identifiedRiskDTO))
        .thenThrow(identifiedProjectNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedRiskDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    riskController.putRisk(identifiedRiskDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutRiskWithNonExistentOwner_shouldRespondWithStatusConflict() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("User not found for id: 1").build();

    // Set up expectations
    when(riskModifier.updateRisk(identifiedRiskDTO)).thenThrow(identifiedUserNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedRiskDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    riskController.putRisk(identifiedRiskDTO);
  }

  @Test
  public void testPutRiskWithValidRisk_shouldRespondWithStatusOkAndReturnUpdatedRisk() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    Risk risk = aRisk().id(1L).build();

    IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    // Set up expectations
    when(riskModifier.updateRisk(identifiedRiskDTO)).thenReturn(risk);
    when(riskDTOFactory.createIdentifiedRiskDTO(risk)).thenReturn(expectedIdentifiedRiskDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedRiskDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.RISKS_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .body(is(convertToJSON(expectedIdentifiedRiskDTO)));

    IdentifiedRiskDTO actualIdentifiedRiskDTO = riskController.putRisk(identifiedRiskDTO);

    // Verify behaviour
    assertThat(actualIdentifiedRiskDTO, is(expectedIdentifiedRiskDTO));
  }
}
