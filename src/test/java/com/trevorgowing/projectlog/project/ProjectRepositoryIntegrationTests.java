package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import com.trevorgowing.projectlog.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ProjectRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests {

    private static final long IRRELEVANT_PROJECT_ID = 1L;
    private static final String PROJECT_ONE_CODE = "project.one.code";
    private static final String PROJECT_ONE_NAME = "project.one.name";

    private static final String PROJECT_TWO_CODE = "project.two.code";
    private static final String PROJECT_TWO_NAME = "project.two.name";
    private static final String OWNER_TWO_EMAIL = "owner.two@trevorgowing.com";
    private static final String OWNER_TWO_PASSWORD = "owner.two.password";
    private static final String OWNER_TWO_FIRST_NAME = "owner.two.first.name";
    private static final String OWNER_TWO_LAST_NAME = "owner.two.last.name";

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testFindIdentifiedProjectsDTOsWithNoExistingProjects_shouldReturnNoIdentifiedDTOs() {
        // Exercise SUT
        List<IdentifiedProjectDTO> actualIdentifiedProjectDTOs = projectRepository.findIdentifiedProjectDTOs();

        // Verify results
        assertThat(actualIdentifiedProjectDTOs, is(empty()));
    }

    @Test
    public void testFindIdentifiedProjectsDTOsWithExistingProjects_shouldReturnIdentifiedDTOs() {
        // Set up fixture
        User ownerOne = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        Project projectOne = aProject()
                .code(PROJECT_ONE_CODE)
                .name(PROJECT_ONE_NAME)
                .owner(ownerOne)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .buildAndPersist(entityManager);

        IdentifiedProjectDTO identifiedProjectOneDTO = anIdentifiedProjectDTO()
                .id(projectOne.getId())
                .code(PROJECT_ONE_CODE)
                .name(PROJECT_ONE_NAME)
                .owner(anIdentifiedUserDTO()
                        .id(ownerOne.getId())
                        .email(IRRELEVANT_USER_EMAIL)
                        .firstName(IRRELEVANT_USER_FIRST_NAME)
                        .lastName(IRRELEVANT_USER_LAST_NAME)
                        .build())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        User ownerTwo = aUser()
                .email(OWNER_TWO_EMAIL)
                .password(OWNER_TWO_PASSWORD)
                .firstName(OWNER_TWO_FIRST_NAME)
                .lastName(OWNER_TWO_LAST_NAME)
                .build();

        Project projectTwo = aProject()
                .code(PROJECT_TWO_CODE)
                .name(PROJECT_TWO_NAME)
                .owner(ownerTwo)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .buildAndPersist(entityManager);

        IdentifiedProjectDTO identifiedProjectTwoDTO = anIdentifiedProjectDTO()
                .id(projectTwo.getId())
                .code(PROJECT_TWO_CODE)
                .name(PROJECT_TWO_NAME)
                .owner(anIdentifiedUserDTO()
                        .id(ownerTwo.getId())
                        .email(OWNER_TWO_EMAIL)
                        .firstName(OWNER_TWO_FIRST_NAME)
                        .lastName(OWNER_TWO_LAST_NAME).build())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        List<IdentifiedProjectDTO> expectedIdentifiedProjectDTOs = asList(identifiedProjectOneDTO, identifiedProjectTwoDTO);

        // Exercise
        List<IdentifiedProjectDTO> actualIdentifiedProjectDTOs = projectRepository.findIdentifiedProjectDTOs();

        // Verify results
        assertThat(actualIdentifiedProjectDTOs, is(expectedIdentifiedProjectDTOs));
    }

    @Test
    public void testFindIdentifiedProjectDTOByIdWithNoMatchingProject_shouldReturnNull() {
        // Exercise SUT
        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectRepository.findIdentifiedProjectDTOById(
                IRRELEVANT_PROJECT_ID);

        // Verify results
        assertThat(actualIdentifiedProjectDTO, is(nullValue(IdentifiedProjectDTO.class)));
    }

    @Test
    public void testFindIdentifiedProjectDTOByIdWithMatchingProject_shouldReturnProject() {
        // Set up fixture
        User ownerOne = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        Project projectOne = aProject()
                .code(PROJECT_ONE_CODE)
                .name(PROJECT_ONE_NAME)
                .owner(ownerOne)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .buildAndPersist(entityManager);

        IdentifiedProjectDTO expectedIdentifiedProjectDTO = anIdentifiedProjectDTO()
                .id(projectOne.getId())
                .code(PROJECT_ONE_CODE)
                .name(PROJECT_ONE_NAME)
                .owner(anIdentifiedUserDTO()
                        .id(ownerOne.getId())
                        .email(IRRELEVANT_USER_EMAIL)
                        .firstName(IRRELEVANT_USER_FIRST_NAME)
                        .lastName(IRRELEVANT_USER_LAST_NAME)
                        .build())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        User ownerTwo = aUser()
                .email(OWNER_TWO_EMAIL)
                .password(OWNER_TWO_PASSWORD)
                .firstName(OWNER_TWO_FIRST_NAME)
                .lastName(OWNER_TWO_LAST_NAME)
                .build();

        aProject()
                .code(PROJECT_TWO_CODE)
                .name(PROJECT_TWO_NAME)
                .owner(ownerTwo)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .buildAndPersist(entityManager);

        // Exercise SUT
        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectRepository.findIdentifiedProjectDTOById(
                projectOne.getId());

        // Verify results
        assertThat(actualIdentifiedProjectDTO, is(expectedIdentifiedProjectDTO));
    }
}