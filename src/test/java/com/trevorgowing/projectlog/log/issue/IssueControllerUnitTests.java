package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static com.trevorgowing.UrlStringBuilder.basedUrlBuilder;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static com.trevorgowing.projectlog.log.issue.UnidentifiedIssueDTOBuilder.anUnidentifiedIssueDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class IssueControllerUnitTests extends AbstractControllerUnitTests {

    @Mock
    private IssueDTOFactory issueDTOFactory;
    @Mock
    private IssueCRUDService issueCRUDService;

    @InjectMocks
    private IssueController issueController;

    @Override
    protected Object getController() {
        return issueController;
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testPostIssueWithNonExistentProject_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO()
                .project(anIdentifiedProjectDTO().id(1L).build())
                .build();

        // Set up expectations
        when(issueCRUDService.createIssue(unidentifiedIssueDTO)).thenThrow(identifiedProjectNotFoundException(1L));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedIssueDTO))
        .when()
                .post(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(LogConstants.ISSUES_URL_PATH).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        issueController.postIssue(unidentifiedIssueDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testPostIssueWithNonExistentOwner_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO()
                .owner(anIdentifiedUserDTO().id(1L).build())
                .build();

        // Set up expectations
        when(issueCRUDService.createIssue(unidentifiedIssueDTO)).thenThrow(identifiedUserNotFoundException(1L));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedIssueDTO))
        .when()
                .post(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(LogConstants.ISSUES_URL_PATH).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        issueController.postIssue(unidentifiedIssueDTO);
    }

    @Test
    public void testPostIssueWithValidIssue_shouldRespondWithStatusCreatedAndReturnIdentifiedIssueDTO() throws Exception {
        // Set up fixture
        UnidentifiedIssueDTO unidentifiedIssueDTO = anUnidentifiedIssueDTO().build();

        Issue issue = anIssue().build();

        IdentifiedIssueDTO expectedIdentifiedIssueDTO = anIdentifiedIssueDTO().build();

        // Set up expectations
        when(issueCRUDService.createIssue(unidentifiedIssueDTO)).thenReturn(issue);
        when(issueDTOFactory.createIdentifiedIssueDTO(issue)).thenReturn(expectedIdentifiedIssueDTO);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedIssueDTO))
        .when()
                .post(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(LogConstants.ISSUES_URL_PATH).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body(is(convertToJSON(expectedIdentifiedIssueDTO)));

        IdentifiedIssueDTO actualIdentifiedIssueDTO = issueController.postIssue(unidentifiedIssueDTO);

        // Verify behaviour
        assertThat(actualIdentifiedIssueDTO, is(expectedIdentifiedIssueDTO));
    }
}