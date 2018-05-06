package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.UrlStringBuilder.basedUrlBuilder;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static com.trevorgowing.projectlog.log.issue.IssueNotFoundException.identifiedIssueNotFoundException;
import static com.trevorgowing.projectlog.log.issue.UnidentifiedIssueDTOBuilder.anUnidentifiedIssueDTO;
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

public class IssueControllerUnitTests extends AbstractControllerUnitTests {

  @Mock private IssueFactory issueFactory;
  @Mock private IssueModifier issueModifier;
  @Mock private IssueDTOFactory issueDTOFactory;

  @InjectMocks private IssueController issueController;

  @Override
  protected Object getController() {
    return issueController;
  }

  @Test(expected = ProjectNotFoundException.class)
  public void testPostIssueWithNonExistentProject_shouldRespondWithStatusConflict() {
    // Set up fixture
    UnidentifiedIssueDTO unidentifiedIssueDTO =
        anUnidentifiedIssueDTO().project(anIdentifiedProjectDTO().id(1L).build()).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("Project not found for id: 1").build();

    // Set up expectations
    when(issueFactory.createIssue(unidentifiedIssueDTO))
        .thenThrow(identifiedProjectNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedIssueDTO))
        .when()
        .post(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    issueController.postIssue(unidentifiedIssueDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testPostIssueWithNonExistentOwner_shouldRespondWithStatusConflict() {
    // Set up fixture
    UnidentifiedIssueDTO unidentifiedIssueDTO =
        anUnidentifiedIssueDTO().owner(anIdentifiedUserDTO().id(1L).build()).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("User not found for id: 1").build();

    // Set up expectations
    when(issueFactory.createIssue(unidentifiedIssueDTO))
        .thenThrow(identifiedUserNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedIssueDTO))
        .when()
        .post(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    issueController.postIssue(unidentifiedIssueDTO);
  }

  @Test
  public void
      testPostIssueWithValidIssue_shouldRespondWithStatusCreatedAndReturnIdentifiedIssueDTO() {
    // Set up fixture
    UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO().build();

    Issue issue = anIssue().build();

    IdentifiedIssueDTO expectedIdentifiedIssueDTO = anIdentifiedIssueDTO().build();

    // Set up expectations
    when(issueFactory.createIssue(unidentifiedIssueDTO)).thenReturn(issue);
    when(issueDTOFactory.createIdentifiedIssueDTO(issue)).thenReturn(expectedIdentifiedIssueDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedIssueDTO))
        .when()
        .post(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedIssueDTO)));

    IdentifiedIssueDTO actualIdentifiedIssueDTO = issueController.postIssue(unidentifiedIssueDTO);

    // Verify behaviour
    assertThat(actualIdentifiedIssueDTO, is(expectedIdentifiedIssueDTO));
  }

  @Test(expected = IssueNotFoundException.class)
  public void testPutIssueWithNoExistingIssue_shouldRespondWithStatusNotFound() {
    // Set up fixture
    IdentifiedIssueDTO identifiedIssueDTO = anIdentifiedIssueDTO().id(1L).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message("Issue not found for id: 1").build();

    // Set up expectations
    when(issueModifier.updateIssue(identifiedIssueDTO))
        .thenThrow(identifiedIssueNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedIssueDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    issueController.putIssue(identifiedIssueDTO);
  }

  @Test(expected = ProjectNotFoundException.class)
  public void testPutIssueWithNonExistentProject_shouldRespondWithStatusConflict() {
    // Set up fixture
    IdentifiedIssueDTO identifiedIssueDTO =
        anIdentifiedIssueDTO().project(anIdentifiedProjectDTO().id(1L).build()).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("Project not found for id: 1").build();

    // Set up expectations
    when(issueModifier.updateIssue(identifiedIssueDTO))
        .thenThrow(identifiedProjectNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedIssueDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    issueController.putIssue(identifiedIssueDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutIssueWithNonExistentOwner_shouldRespondWithStatusConflict() {
    // Set up fixture
    IdentifiedIssueDTO identifiedIssueDTO =
        anIdentifiedIssueDTO().owner(anIdentifiedUserDTO().id(1L).build()).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder().status(CONFLICT).message("User not found for id: 1").build();

    // Set up expectations
    when(issueModifier.updateIssue(identifiedIssueDTO))
        .thenThrow(identifiedUserNotFoundException(1L));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedIssueDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    issueController.putIssue(identifiedIssueDTO);
  }

  @Test
  public void testPutIssueWithValidIssue_shouldRespondWithStatusOkAndReturnIdentifiedIssueDTO() {
    // Set up fixture
    IdentifiedIssueDTO identifiedIssueDTO = anIdentifiedIssueDTO().build();

    Issue issue = anIssue().build();

    IdentifiedIssueDTO expectedIdentifiedIssueDTO = anIdentifiedIssueDTO().build();

    // Set up expectations
    when(issueModifier.updateIssue(identifiedIssueDTO)).thenReturn(issue);
    when(issueDTOFactory.createIdentifiedIssueDTO(issue)).thenReturn(expectedIdentifiedIssueDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedIssueDTO))
        .when()
        .put(
            basedUrlBuilder(LogConstants.LOGS_URL_PATH)
                .appendPath(LogConstants.ISSUES_URL_PATH)
                .toString())
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedIssueDTO)));

    IdentifiedIssueDTO actualIdentifiedIssueDTO = issueController.putIssue(identifiedIssueDTO);

    // Verify behaviour
    assertThat(actualIdentifiedIssueDTO, (is(expectedIdentifiedIssueDTO)));
  }
}
