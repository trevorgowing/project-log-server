package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.project.DuplicateProjectCodeException.codedDuplicateCodeException;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.project.UnidentifiedProjectDTOBuilder.anUnidentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ProjectControllerUnitTests extends AbstractControllerUnitTests {

    private static final long IRRELEVANT_PROJECT_ID = 1L;
    private static final String IRRELEVANT_PROJECT_CODE = "irrelevant.project.code";
    private static final String IRRELEVANT_PROJECT_NAME = "irrelevant.project.name";
    private static final IdentifiedUserDTO IRRELEVANT_OWNER = anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    @Mock
    private ProjectDTOFactory projectDTOFactory;
    @Mock
    private ProjectCRUDService projectCRUDService;

    @InjectMocks
    private ProjectController projectController;

    @Override
    protected Object getController() {
        return projectController;
    }

    @Test
    public void testGetProjectsWithNoExistingProjects_shouldRespondWithStatusOKAndReturnNoProjects() throws Exception {
        // Set up expectations
        when(projectCRUDService.getIdentifiedProjectDTOs()).thenReturn(Collections.emptyList());

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(ProjectConstants.PROJECTS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(Collections.<IdentifiedProjectDTO>emptyList())));

        List<IdentifiedProjectDTO> actualProjects = projectController.getProjects();

        // Verify behaviour
        assertThat(actualProjects, is(empty()));
    }

    @Test
    public void testGetProjectsWithExistingUsers_shouldRespondWithStatusOKAndReturnProjects() throws Exception {
        // Set up fixture
        IdentifiedProjectDTO identifiedProjectOneDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(IRRELEVANT_OWNER)
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        IdentifiedProjectDTO identifiedProjectTwoDTO = anIdentifiedProjectDTO()
                .id(2L)
                .code("project.two.code")
                .name("project.two.name")
                .owner(anIdentifiedUserDTO()
                        .id(2L)
                        .email("owner.two@trevorgowing.com")
                        .build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        List<IdentifiedProjectDTO> expectedIdentifiedProjectDTOs = asList(identifiedProjectOneDTO,
                identifiedProjectTwoDTO);

        // Set up expectations
        when(projectCRUDService.getIdentifiedProjectDTOs()).thenReturn(expectedIdentifiedProjectDTOs);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(ProjectConstants.PROJECTS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedIdentifiedProjectDTOs)));

        List<IdentifiedProjectDTO> actualIdentifiedProjectDTOs = projectController.getProjects();

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTOs, is(expectedIdentifiedProjectDTOs));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testGetProjectByIdWithNoMatchingProject_shouldRespondWithStatusNotFound() {
        // Set up expectations
        when(projectCRUDService.getIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID))
                .thenThrow(identifiedProjectNotFoundException(IRRELEVANT_PROJECT_ID));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(ProjectConstants.PROJECTS_URL_PATH + "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());

        projectController.getProjectById(IRRELEVANT_PROJECT_ID);
    }

    @Test
    public void testGetProjectByIdWithMatchingProject_shouldRespondWithStatusOKAndReturnProject() throws Exception {
        // Set up fixture
        IdentifiedProjectDTO expectedIdentifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(IRRELEVANT_OWNER)
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectCRUDService.getIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID))
                .thenReturn(expectedIdentifiedProjectDTO);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
        .when()
                .get(ProjectConstants.PROJECTS_URL_PATH + "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(sameBeanAs(convertToJSON(expectedIdentifiedProjectDTO)));

        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectController.getProjectById(IRRELEVANT_PROJECT_ID);

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTO, is(expectedIdentifiedProjectDTO));
    }

    @Test(expected = DuplicateProjectCodeException.class)
    public void testPostProjectWithDuplicateCode_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        String duplicateProjectCode = IRRELEVANT_PROJECT_CODE;
        LocalDate startDate = IRRELEVANT_DATE;
        LocalDate endDate = IRRELEVANT_DATE;

        UnidentifiedProjectDTO unidentifiedProjectDTO = anUnidentifiedProjectDTO()
                .code(duplicateProjectCode)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(IRRELEVANT_USER_ID).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Set up expectations
        when(projectCRUDService.createProject(duplicateProjectCode, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, startDate, endDate))
                .thenThrow(codedDuplicateCodeException(duplicateProjectCode));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedProjectDTO))
        .when()
                .post(ProjectConstants.PROJECTS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        projectController.postProject(unidentifiedProjectDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testPostProjectWithNonExistentUser_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        long nonExistentUserId = 111L;

        UnidentifiedProjectDTO unidentifiedProjectDTO = anUnidentifiedProjectDTO()
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(nonExistentUserId).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectCRUDService.createProject(IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME, nonExistentUserId,
                IRRELEVANT_DATE, IRRELEVANT_DATE))
                .thenThrow(identifiedUserNotFoundException(nonExistentUserId));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedProjectDTO))
        .when()
                .post(ProjectConstants.PROJECTS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        projectController.postProject(unidentifiedProjectDTO);
    }

    @Test
    public void testPostProjectWithValidProject_shouldRespondWithStatusCreatedAndReturnCreatedProject() throws Exception {
        // Set up fixture
        UnidentifiedProjectDTO unidentifiedProjectDTO = anUnidentifiedProjectDTO()
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        Project project = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(aUser().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        IdentifiedProjectDTO expectedIdentifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectCRUDService.createProject(IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME, IRRELEVANT_USER_ID, IRRELEVANT_DATE, IRRELEVANT_DATE))
                .thenReturn(project);
        when(projectDTOFactory.createIdentifiedProjectDTO(project))
                .thenReturn(expectedIdentifiedProjectDTO);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedProjectDTO))
        .when()
                .post(ProjectConstants.PROJECTS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedIdentifiedProjectDTO)));

        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectController.postProject(unidentifiedProjectDTO);

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTO, is(expectedIdentifiedProjectDTO));
    }

    @Test(expected = DuplicateProjectCodeException.class)
    public void testPutProjectWithDuplicateCode_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        String duplicateProjectCode = IRRELEVANT_PROJECT_CODE;

        IdentifiedProjectDTO identifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(duplicateProjectCode)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectCRUDService.updateProject(IRRELEVANT_PROJECT_ID, duplicateProjectCode, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, IRRELEVANT_DATE, IRRELEVANT_DATE))
                .thenThrow(codedDuplicateCodeException(duplicateProjectCode));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(identifiedProjectDTO))
        .when()
                .put(ProjectConstants.PROJECTS_URL_PATH + "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        projectController.putProject(IRRELEVANT_PROJECT_ID, identifiedProjectDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testPutProjectWithNonExistentUser_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        long nonExistentUserId = IRRELEVANT_USER_ID;

        IdentifiedProjectDTO identifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(nonExistentUserId).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectCRUDService.updateProject(IRRELEVANT_PROJECT_ID, IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME,
                nonExistentUserId, IRRELEVANT_DATE, IRRELEVANT_DATE))
                .thenThrow(identifiedUserNotFoundException(nonExistentUserId));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(identifiedProjectDTO))
        .when()
                .put(ProjectConstants.PROJECTS_URL_PATH + "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        projectController.putProject(IRRELEVANT_PROJECT_ID, identifiedProjectDTO);
    }

    @Test
    public void testPutProjectWithValidProject_shouldRespondWithStatusOKAndReturnUpdatedProject() throws Exception {
        // Set up fixture
        IdentifiedProjectDTO identifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        Project project = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(aUser().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        IdentifiedProjectDTO expectedIdentifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(anIdentifiedUserDTO().id(IRRELEVANT_USER_ID).build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectCRUDService.updateProject(IRRELEVANT_PROJECT_ID, IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, IRRELEVANT_DATE, IRRELEVANT_DATE))
                .thenReturn(project);
        when(projectDTOFactory.createIdentifiedProjectDTO(project))
                .thenReturn(expectedIdentifiedProjectDTO);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(identifiedProjectDTO))
        .when()
                .put(ProjectConstants.PROJECTS_URL_PATH + "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedIdentifiedProjectDTO)));

        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectController.putProject(IRRELEVANT_PROJECT_ID,
                identifiedProjectDTO);

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTO, is(expectedIdentifiedProjectDTO));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testDeleteProjectWithNoMatchingProject_shouldRespondWithStatusNotFound() {
        // Set up expectations
        doThrow(identifiedProjectNotFoundException(IRRELEVANT_PROJECT_ID))
                .when(projectCRUDService).deleteProject(IRRELEVANT_PROJECT_ID);

        // Exercise SUT
        given()
        .when()
                .delete(ProjectConstants.PROJECTS_URL_PATH+ "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());

        projectController.deleteProject(IRRELEVANT_PROJECT_ID);
    }

    @Test
    public void testDeleteProjectWithMatchingProject_shouldRespondWithStatusNoContent() {
        // Set up expectations
        doNothing().when(projectCRUDService).deleteProject(IRRELEVANT_PROJECT_ID);

        // Exercise SUT
        given()
        .when()
                .delete(ProjectConstants.PROJECTS_URL_PATH + "/" + IRRELEVANT_PROJECT_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        projectController.deleteProject(IRRELEVANT_PROJECT_ID);
    }
}
