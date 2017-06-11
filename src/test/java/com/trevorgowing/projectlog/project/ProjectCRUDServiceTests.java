package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserCRUDService;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ProjectCRUDServiceTests extends AbstractTests {

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
    private UserCRUDService userCRUDService;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectCRUDService projectCRUDService;

    @Test(expected = ProjectNotFoundException.class)
    public void testFindProjectWithNoMatchingProject_shouldThrowProjectNotFoundException() {
        // Set up expectations
        when(projectRepository.findOne(IRRELEVANT_PROJECT_ID))
                .thenReturn(null);

        // Exercise SUT
        projectCRUDService.findProject(IRRELEVANT_PROJECT_ID);
    }

    @Test
    public void testFindProjectWithMatchingProject_shouldReturnProject() {
        // Set up fixture
        Project expectedProject = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .build();

        // Set up expectations
        when(projectRepository.findOne(IRRELEVANT_PROJECT_ID))
                .thenReturn(expectedProject);

        // Exercise SUT
        Project actualProject = projectCRUDService.findProject(IRRELEVANT_PROJECT_ID);

        // Verify behaviour
        assertThat(actualProject, is(expectedProject));
    }

    @Test
    public void testGetIdentifiedProjectDTOs_shouldDelegateToProjectRepositoryAndReturnIdentifiedProjectDTOs() {
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
                        .firstName("owner.two.first.name")
                        .lastName("owner.two.last.name")
                        .build())
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        List<IdentifiedProjectDTO> expectedIdentifiedProjectDTOs = asList(identifiedProjectOneDTO,
                identifiedProjectTwoDTO);

        // Set up expectations
        when(projectRepository.findIdentifiedProjectDTOs()).thenReturn(expectedIdentifiedProjectDTOs);

        // Exercise SUT
        List<IdentifiedProjectDTO> actualIdentifiedProjectDTOs = projectCRUDService.getIdentifiedProjectDTOs();

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTOs, is(expectedIdentifiedProjectDTOs));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testGetIdentifiedProjectDTOByIdWithNoMatchingProject_shouldThrowProjectNotFoundException() {
        // Set up expectations
        when(projectRepository.findIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID))
                .thenReturn(null);

        // Exercise SUT
        projectCRUDService.getIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID);
    }

    @Test
    public void testGetIdentifiedProjectDTOByIdWithMatchingProject_shouldReturnProject() {
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
        when(projectRepository.findIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID))
                .thenReturn(expectedIdentifiedProjectDTO);

        // Exercise SUT
        IdentifiedProjectDTO actualIdentifiedProjectDTO = projectCRUDService.getIdentifiedProjectDTOById(
                IRRELEVANT_PROJECT_ID);

        // Verify behaviour
        assertThat(actualIdentifiedProjectDTO, is(expectedIdentifiedProjectDTO));
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateProjectWithNonExistentUser_shouldDelegateToUserCRUDServiceAndThrowUserNotFoundException() {
        // Set up fixture
        long nonExistentUserID = IRRELEVANT_USER_ID;

        // Set up expectations
        when(userCRUDService.findUser(nonExistentUserID))
                .thenThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID));

        // Exercise SUT
        projectCRUDService.createProject(IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME,
                nonExistentUserID, IRRELEVANT_DATE, IRRELEVANT_DATE);
    }

    @Test(expected = DuplicateProjectCodeException.class)
    public void testCreateProjectWithDuplicateCode_shouldDelegateToProjectRepositoryAndThrowDuplicateProjectCodeException() {
        // Set up fixture
        String duplicateCode = IRRELEVANT_PROJECT_CODE;

        User identifiedUser = aUser()
                .id(IRRELEVANT_USER_ID)
                .build();

        Project unidentifiedProject = aProject()
                .code(duplicateCode)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(identifiedUser)
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(userCRUDService.findUser(IRRELEVANT_USER_ID))
                .thenReturn(identifiedUser);
        when(projectRepository.save(unidentifiedProject))
                .thenThrow(new DataIntegrityViolationException(IRRELEVANT_MESSAGE));

        // Exercise SUT
        projectCRUDService.createProject(duplicateCode, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, IRRELEVANT_DATE, IRRELEVANT_DATE);
    }

    @Test
    public void testCreateProjectWithValidProject_shouldDelegateToProjectRepositoryToSaveCreatedProjectAndReturnManagedProject() {
        // Set up fixture
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(1);

        Project unidentifiedProject = aProject()
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(aUser().id(IRRELEVANT_USER_ID).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Project expectedProject = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(aUser().id(IRRELEVANT_USER_ID).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Set up expectations
        when(projectRepository.save(unidentifiedProject)).thenReturn(expectedProject);

        // Exercise SUT
        Project actualProject = projectCRUDService.createProject(IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, startDate, endDate);

        // Verify behaviour
        assertThat(actualProject, is(expectedProject));
    }

    @Test(expected = DuplicateProjectCodeException.class)
    public void testUpdateProjectWithDuplicateCode_shouldDelegateToProjectRepositoryAndThrowDuplicateProjectCodeException() {
        // Set up fixture
        String duplicateCode = IRRELEVANT_PROJECT_CODE;
        String duplicateCodeMessage = IRRELEVANT_MESSAGE;

        User identifiedUser = aUser()
                .id(IRRELEVANT_USER_ID)
                .build();

        Project projectPreUpdate = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(duplicateCode)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(identifiedUser)
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        Project updatedProject = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(duplicateCode)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(identifiedUser)
                .startDate(IRRELEVANT_DATE)
                .endDate(IRRELEVANT_DATE)
                .build();

        // Set up expectations
        when(projectRepository.findOne(IRRELEVANT_PROJECT_ID))
                .thenReturn(projectPreUpdate);
        when(userCRUDService.findUser(IRRELEVANT_USER_ID))
                .thenReturn(identifiedUser);
        when(projectRepository.save(argThat(sameBeanAs(updatedProject))))
                .thenThrow(new DataIntegrityViolationException(duplicateCodeMessage));

        // Exercise SUT
        projectCRUDService.updateProject(IRRELEVANT_PROJECT_ID, duplicateCode, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, IRRELEVANT_DATE, IRRELEVANT_DATE);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateProjectWithNonExistentUser_shouldDelegateToUserCRUDServiceAndThrowUserNotFoundException() {
        // Set up fixture
        long nonExistentUserId = IRRELEVANT_USER_ID;

        Project projectPreUpdate = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .build();

        // Set up expectations
        when(projectRepository.findOne(IRRELEVANT_PROJECT_ID))
                .thenReturn(projectPreUpdate);
        when(userCRUDService.findUser(nonExistentUserId))
                .thenThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID));

        // Exercise SUT
        projectCRUDService.updateProject(IRRELEVANT_PROJECT_ID, IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME,
                nonExistentUserId, IRRELEVANT_DATE, IRRELEVANT_DATE);
    }

    @Test
    public void testUpdateProjectWithValidProject_shouldDelegateToProjectRepositoryAndReturnUpdatedProject() {
        // Set up fixture
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(1);

        Project projectPreUpdate = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code("unchanged_code")
                .name("unchanged_name")
                .owner(aUser().build())
                .startDate(LocalDate.MIN)
                .endDate(LocalDate.MAX)
                .build();

        User user = aUser()
                .id(IRRELEVANT_USER_ID)
                .build();

        Project expectedProject = aProject()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_PROJECT_CODE)
                .name(IRRELEVANT_PROJECT_NAME)
                .owner(user)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Set up expectations
        when(projectRepository.findOne(IRRELEVANT_PROJECT_ID))
                .thenReturn(projectPreUpdate);
        when(userCRUDService.findUser(IRRELEVANT_USER_ID))
                .thenReturn(user);
        when(projectRepository.save(argThat(sameBeanAs(expectedProject))))
                .thenReturn(expectedProject);

        // Exercise SUT
        Project actualProject = projectCRUDService.updateProject(IRRELEVANT_PROJECT_ID, IRRELEVANT_PROJECT_CODE, IRRELEVANT_PROJECT_NAME,
                IRRELEVANT_USER_ID, startDate, endDate);

        // Verify behaviour
        assertThat(actualProject, is(expectedProject));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testDeleteProjectWithNoMatchingProject_shouldThrowProjectNotFoundException() {
        // Set up expectations
        doThrow(new EmptyResultDataAccessException(1))
                .when(projectRepository).delete(IRRELEVANT_PROJECT_ID);

        // Exercise SUT
        projectCRUDService.deleteProject(IRRELEVANT_PROJECT_ID);
    }

    @Test
    public void testDeleteProjectWithMatchingProject_shouldDelegateToProjectRepositoryToDeleteProject() {
        // Set up expectations
        doNothing().when(projectRepository).delete(IRRELEVANT_PROJECT_ID);

        // Exercise SUT
        projectCRUDService.deleteProject(IRRELEVANT_PROJECT_ID);
    }
}