package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSON.convertToJSON;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ProjectControllerUnitTests extends AbstractControllerUnitTests {

    private static final long IRRELEVANT_PROJECT_ID = 1L;
    private static final String IRRELEVANT_CODE = "irrelevant.project.code";
    private static final String IRRELEVANT_NAME = "irrelevant.project.name";
    private static final IdentifiedUserDTO IRRELEVANT_OWNER = anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

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
                .code(IRRELEVANT_CODE)
                .name(IRRELEVANT_NAME)
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
}
