package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import com.trevorgowing.projectlog.user.UserRetriever;
import java.time.LocalDate;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

public class ProjectModifierTests extends AbstractTests {

  private static final long IRRELEVANT_PROJECT_ID = 1L;
  private static final String IRRELEVANT_PROJECT_CODE = "irrelevant.project.code";
  private static final String IRRELEVANT_PROJECT_NAME = "irrelevant.project.name";

  @Mock private UserRetriever userRetriever;
  @Mock private ProjectRetriever projectRetriever;
  @Mock private ProjectRepository projectRepository;

  @InjectMocks private ProjectModifier projectModifier;

  @Test(expected = DuplicateProjectCodeException.class)
  public void
      testUpdateProjectWithDuplicateCode_shouldDelegateToProjectRepositoryAndThrowDuplicateProjectCodeException() {
    // Set up fixture
    String duplicateCode = IRRELEVANT_PROJECT_CODE;
    String duplicateCodeMessage = IRRELEVANT_MESSAGE;

    User identifiedUser = aUser().id(IRRELEVANT_USER_ID).build();

    Project projectPreUpdate =
        aProject()
            .id(IRRELEVANT_PROJECT_ID)
            .code(duplicateCode)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(identifiedUser)
            .startDate(IRRELEVANT_DATE)
            .endDate(IRRELEVANT_DATE)
            .build();

    Project updatedProject =
        aProject()
            .id(IRRELEVANT_PROJECT_ID)
            .code(duplicateCode)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(identifiedUser)
            .startDate(IRRELEVANT_DATE)
            .endDate(IRRELEVANT_DATE)
            .build();

    // Set up expectations
    when(projectRetriever.findProject(IRRELEVANT_PROJECT_ID)).thenReturn(projectPreUpdate);
    when(userRetriever.findUser(IRRELEVANT_USER_ID)).thenReturn(identifiedUser);
    when(projectRepository.save(argThat(samePropertyValuesAs(updatedProject))))
        .thenThrow(new DataIntegrityViolationException(duplicateCodeMessage));

    // Exercise SUT
    projectModifier.updateProject(
        IRRELEVANT_PROJECT_ID,
        duplicateCode,
        IRRELEVANT_PROJECT_NAME,
        IRRELEVANT_USER_ID,
        IRRELEVANT_DATE,
        IRRELEVANT_DATE);
  }

  @Test(expected = UserNotFoundException.class)
  public void
      testUpdateProjectWithNonExistentUser_shouldDelegateToUserCRUDServiceAndThrowUserNotFoundException() {
    // Set up fixture
    long nonExistentUserId = IRRELEVANT_USER_ID;

    Project projectPreUpdate = aProject().id(IRRELEVANT_PROJECT_ID).build();

    // Set up expectations
    when(projectRetriever.findProject(IRRELEVANT_PROJECT_ID)).thenReturn(projectPreUpdate);
    when(userRetriever.findUser(nonExistentUserId))
        .thenThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID));

    // Exercise SUT
    projectModifier.updateProject(
        IRRELEVANT_PROJECT_ID,
        IRRELEVANT_PROJECT_CODE,
        IRRELEVANT_PROJECT_NAME,
        nonExistentUserId,
        IRRELEVANT_DATE,
        IRRELEVANT_DATE);
  }

  @Test
  public void
      testUpdateProjectWithValidProject_shouldDelegateToProjectRepositoryAndReturnUpdatedProject() {
    // Set up fixture
    LocalDate startDate = LocalDate.now().plusDays(1);
    LocalDate endDate = startDate.plusDays(1);

    Project projectPreUpdate =
        aProject()
            .id(IRRELEVANT_PROJECT_ID)
            .code("unchanged_code")
            .name("unchanged_name")
            .owner(aUser().build())
            .startDate(LocalDate.MIN)
            .endDate(LocalDate.MAX)
            .build();

    User user = aUser().id(IRRELEVANT_USER_ID).build();

    Project expectedProject =
        aProject()
            .id(IRRELEVANT_PROJECT_ID)
            .code(IRRELEVANT_PROJECT_CODE)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(user)
            .startDate(startDate)
            .endDate(endDate)
            .build();

    // Set up expectations
    when(projectRetriever.findProject(IRRELEVANT_PROJECT_ID)).thenReturn(projectPreUpdate);
    when(userRetriever.findUser(IRRELEVANT_USER_ID)).thenReturn(user);
    when(projectRepository.save(argThat(samePropertyValuesAs(expectedProject))))
        .thenReturn(expectedProject);

    // Exercise SUT
    Project actualProject =
        projectModifier.updateProject(
            IRRELEVANT_PROJECT_ID,
            IRRELEVANT_PROJECT_CODE,
            IRRELEVANT_PROJECT_NAME,
            IRRELEVANT_USER_ID,
            startDate,
            endDate);

    // Verify behaviour
    assertThat(actualProject, is(expectedProject));
  }
}
