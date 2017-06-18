package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.User;
import org.junit.Test;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class ProjectDTOFactoryTests extends AbstractTests {

    private static final long IRRELEVANT_PROJECT_ID = 1L;
    private static final String IRRELEVANT_PROJECT_CODE = "irrelevant.project.code";
    private static final String IRRELEVANT_PROJECT_NAME = "irrelevant.project.name";
    private static final User IRRELEVANT_OWNER = aUser()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    private ProjectDTOFactory projectDTOFactory = new ProjectDTOFactory();

    @Test
    public void testCreateIdentifiedProjectDTO_shouldCreateIdentifiedProjectDTOFromGivenProject() {
        // Set up fixture
        LocalDate startDate = IRRELEVANT_DATE;
        LocalDate endDate = IRRELEVANT_DATE;

        Project project = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(IRRELEVANT_OWNER)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        IdentifiedUserDTO identifiedUserDTO = anIdentifiedUserDTO()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();
        IdentifiedProjectDTO expectedIdentifiedProjectDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(identifiedUserDTO)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Exercise SUT
        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectDTOFactory.createIdentifiedProjectDTO(project);

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTO, samePropertyValuesAs(expectedIdentifiedProjectDTO));
    }
}
