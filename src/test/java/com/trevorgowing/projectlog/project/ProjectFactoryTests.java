package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import com.trevorgowing.projectlog.user.UserRetriever;
import java.time.LocalDate;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

public class ProjectFactoryTests extends AbstractTests {

  private static final long IRRELEVANT_PROJECT_ID = 1L;
  private static final String IRRELEVANT_PROJECT_CODE = "irrelevant.project.code";
  private static final String IRRELEVANT_PROJECT_NAME = "irrelevant.project.name";
  private static final IdentifiedUserDTO IRRELEVANT_OWNER =
      anIdentifiedUserDTO()
          .id(IRRELEVANT_USER_ID)
          .email(IRRELEVANT_USER_EMAIL)
          .firstName(IRRELEVANT_USER_FIRST_NAME)
          .lastName(IRRELEVANT_USER_LAST_NAME)
          .build();

  @Mock private UserRetriever userRetriever;
  @Mock private ProjectRepository projectRepository;

  @InjectMocks private ProjectFactory projectFactory;

  @Test(expected = UserNotFoundException.class)
  public void
      testCreateProjectWithNonExistentUser_shouldDelegateToUserCRUDServiceAndThrowUserNotFoundException() {
    // Set up fixture
    long nonExistentUserID = IRRELEVANT_USER_ID;

    // Set up expectations
    when(userRetriever.findUser(nonExistentUserID))
        .thenThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID));

    // Exercise SUT
    projectFactory.createProject(
        IRRELEVANT_PROJECT_CODE,
        IRRELEVANT_PROJECT_NAME,
        nonExistentUserID,
        IRRELEVANT_DATE,
        IRRELEVANT_DATE);
  }

  @Test(expected = DuplicateProjectCodeException.class)
  public void
      testCreateProjectWithDuplicateCode_shouldDelegateToProjectRepositoryAndThrowDuplicateProjectCodeException() {
    // Set up fixture
    String duplicateCode = IRRELEVANT_PROJECT_CODE;

    User owner = aUser().id(IRRELEVANT_USER_ID).build();

    Project unidentifiedProject =
        aProject()
            .code(duplicateCode)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(owner)
            .startDate(IRRELEVANT_DATE)
            .endDate(IRRELEVANT_DATE)
            .build();

    // Set up expectations
    when(userRetriever.findUser(IRRELEVANT_USER_ID)).thenReturn(owner);
    when(projectRepository.save(unidentifiedProject))
        .thenThrow(new DataIntegrityViolationException(IRRELEVANT_MESSAGE));

    // Exercise SUT
    projectFactory.createProject(
        duplicateCode,
        IRRELEVANT_PROJECT_NAME,
        IRRELEVANT_USER_ID,
        IRRELEVANT_DATE,
        IRRELEVANT_DATE);
  }

  @Test
  public void
      testCreateProjectWithValidProject_shouldDelegateToProjectRepositoryToSaveCreatedProjectAndReturnManagedProject() {
    // Set up fixture
    LocalDate startDate = LocalDate.now().plusDays(1);
    LocalDate endDate = startDate.plusDays(1);

    User owner = aUser().id(IRRELEVANT_USER_ID).build();

    Project unidentifiedProject =
        aProject()
            .code(IRRELEVANT_PROJECT_CODE)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(owner)
            .startDate(startDate)
            .endDate(endDate)
            .build();

    Project expectedProject =
        aProject()
            .id(IRRELEVANT_PROJECT_ID)
            .code(IRRELEVANT_PROJECT_CODE)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(aUser().id(IRRELEVANT_USER_ID).build())
            .startDate(startDate)
            .endDate(endDate)
            .build();

    // Set up expectations
    when(userRetriever.findUser(IRRELEVANT_USER_ID)).thenReturn(owner);
    when(projectRepository.save(argThat(samePropertyValuesAs(unidentifiedProject))))
        .thenReturn(expectedProject);

    // Exercise SUT
    Project actualProject =
        projectFactory.createProject(
            IRRELEVANT_PROJECT_CODE,
            IRRELEVANT_PROJECT_NAME,
            IRRELEVANT_USER_ID,
            startDate,
            endDate);

    // Verify behaviour
    assertThat(actualProject, is(expectedProject));
  }
}
