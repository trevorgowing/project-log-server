package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ProjectRetrieverTests extends AbstractTests {

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

  @Mock private ProjectRepository projectRepository;

  @InjectMocks private ProjectRetriever projectRetriever;

  @Test(expected = ProjectNotFoundException.class)
  public void testFindProjectWithNoMatchingProject_shouldThrowProjectNotFoundException() {
    // Set up expectations
    when(projectRepository.findOne(IRRELEVANT_PROJECT_ID)).thenReturn(null);

    // Exercise SUT
    projectRetriever.findProject(IRRELEVANT_PROJECT_ID);
  }

  @Test
  public void testFindProjectWithMatchingProject_shouldReturnProject() {
    // Set up fixture
    Project expectedProject = aProject().id(IRRELEVANT_PROJECT_ID).build();

    // Set up expectations
    when(projectRepository.findOne(IRRELEVANT_PROJECT_ID)).thenReturn(expectedProject);

    // Exercise SUT
    Project actualProject = projectRetriever.findProject(IRRELEVANT_PROJECT_ID);

    // Verify behaviour
    assertThat(actualProject, is(expectedProject));
  }

  @Test
  public void
      testGetIdentifiedProjectDTOs_shouldDelegateToProjectRepositoryAndReturnIdentifiedProjectDTOs() {
    // Set up fixture
    IdentifiedProjectDTO identifiedProjectOneDTO =
        anIdentifiedProjectDTO()
            .id(IRRELEVANT_PROJECT_ID)
            .code(IRRELEVANT_PROJECT_CODE)
            .name(IRRELEVANT_PROJECT_NAME)
            .owner(IRRELEVANT_OWNER)
            .startDate(IRRELEVANT_DATE)
            .endDate(IRRELEVANT_DATE)
            .build();

    IdentifiedProjectDTO identifiedProjectTwoDTO =
        anIdentifiedProjectDTO()
            .id(2L)
            .code("project.two.code")
            .name("project.two.name")
            .owner(
                anIdentifiedUserDTO()
                    .id(2L)
                    .email("owner.two@trevorgowing.com")
                    .firstName("owner.two.first.name")
                    .lastName("owner.two.last.name")
                    .build())
            .startDate(IRRELEVANT_DATE)
            .endDate(IRRELEVANT_DATE)
            .build();

    List<IdentifiedProjectDTO> expectedIdentifiedProjectDTOs =
        asList(identifiedProjectOneDTO, identifiedProjectTwoDTO);

    // Set up expectations
    when(projectRepository.findIdentifiedProjectDTOs()).thenReturn(expectedIdentifiedProjectDTOs);

    // Exercise SUT
    List<IdentifiedProjectDTO> actualIdentifiedProjectDTOs =
        projectRetriever.getIdentifiedProjectDTOs();

    // Verify behaviour
    assertThat(actualIdentifiedProjectDTOs, is(expectedIdentifiedProjectDTOs));
  }

  @Test(expected = ProjectNotFoundException.class)
  public void
      testGetIdentifiedProjectDTOByIdWithNoMatchingProject_shouldThrowProjectNotFoundException() {
    // Set up expectations
    when(projectRepository.findIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID)).thenReturn(null);

    // Exercise SUT
    projectRetriever.getIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID);
  }

  @Test
  public void testGetIdentifiedProjectDTOByIdWithMatchingProject_shouldReturnProject() {
    // Set up fixture
    IdentifiedProjectDTO expectedIdentifiedProjectDTO =
        anIdentifiedProjectDTO()
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
    IdentifiedProjectDTO actualIdentifiedProjectDTO =
        projectRetriever.getIdentifiedProjectDTOById(IRRELEVANT_PROJECT_ID);

    // Verify behaviour
    assertThat(actualIdentifiedProjectDTO, is(expectedIdentifiedProjectDTO));
  }
}
