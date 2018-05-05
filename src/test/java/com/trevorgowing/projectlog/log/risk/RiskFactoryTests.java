package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.log.risk.UnidentifiedRiskDTOBuilder.anUnidentifiedRiskDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RiskFactoryTests extends AbstractTests {

  @Mock private UserRetriever userRetriever;
  @Mock private RiskRepository riskRepository;
  @Mock private ProjectRetriever projectRetriever;

  @InjectMocks private RiskFactory riskFactory;

  @Test(expected = ProjectNotFoundException.class)
  public void testCreateRiskWithNonExistentProject_shouldThrowProjectNotFoundException() {
    // Set up fixture
    UnidentifiedRiskDTO unidentifiedRiskDTO =
        anUnidentifiedRiskDTO()
            .summary("Summary")
            .description("Description")
            .category(Category.COMMITTED_PEOPLE)
            .impact(Impact.MODERATE)
            .status(LogStatus.NEW)
            .project(anIdentifiedProjectDTO().id(1L).build())
            .owner(anIdentifiedUserDTO().id(1L).build())
            .probability(Probability.POSSIBLE)
            .riskResponse(RiskResponse.ACCEPT)
            .build();

    // Set up expectations
    when(projectRetriever.findProject(1L)).thenThrow(identifiedProjectNotFoundException(1L));

    // Exercise SUT
    riskFactory.createRisk(unidentifiedRiskDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testCreateRiskWithNonExistentOwner_shouldThrowUserNotFoundException() {
    // Set up fixture
    UnidentifiedRiskDTO unidentifiedRiskDTO =
        anUnidentifiedRiskDTO()
            .summary("Summary")
            .description("Description")
            .category(Category.COMMITTED_PEOPLE)
            .impact(Impact.MODERATE)
            .status(LogStatus.NEW)
            .project(anIdentifiedProjectDTO().id(1).build())
            .owner(anIdentifiedUserDTO().id(1).build())
            .probability(Probability.POSSIBLE)
            .riskResponse(RiskResponse.ACCEPT)
            .build();

    Project project = aProject().id(1L).build();

    // Set up expectations
    when(projectRetriever.findProject(1L)).thenReturn(project);
    when(userRetriever.findUser(1L)).thenThrow(identifiedUserNotFoundException(1L));

    // Exercise SUT
    riskFactory.createRisk(unidentifiedRiskDTO);
  }

  @Test
  public void
      testCreateRiskWithValidRisk_shouldDelegateToRiskRepositoryToSaveRiskAndReturnManagedRisk() {
    // Set up fixture
    UnidentifiedRiskDTO unidentifiedRiskDTO =
        anUnidentifiedRiskDTO()
            .summary("Summary")
            .description("Description")
            .category(Category.COMMITTED_PEOPLE)
            .impact(Impact.MODERATE)
            .status(LogStatus.NEW)
            .project(anIdentifiedProjectDTO().id(1).build())
            .owner(anIdentifiedUserDTO().id(1).build())
            .probability(Probability.POSSIBLE)
            .riskResponse(RiskResponse.ACCEPT)
            .build();

    Project project = aProject().id(1L).build();
    User owner = aUser().id(1L).build();

    Risk unidentifiedRisk =
        aRisk()
            .summary("Summary")
            .description("Description")
            .category(Category.COMMITTED_PEOPLE)
            .impact(Impact.MODERATE)
            .status(LogStatus.NEW)
            .project(project)
            .owner(owner)
            .probability(Probability.POSSIBLE)
            .riskResponse(RiskResponse.ACCEPT)
            .build();

    Risk expectedRisk =
        aRisk()
            .id(1)
            .summary("Summary")
            .description("Description")
            .category(Category.COMMITTED_PEOPLE)
            .impact(Impact.MODERATE)
            .status(LogStatus.NEW)
            .project(project)
            .owner(owner)
            .probability(Probability.POSSIBLE)
            .riskResponse(RiskResponse.ACCEPT)
            .build();

    // Set up expectations
    when(projectRetriever.findProject(1L)).thenReturn(project);
    when(userRetriever.findUser(1L)).thenReturn(owner);
    when(riskRepository.save(argThat(samePropertyValuesAs(unidentifiedRisk))))
        .thenReturn(expectedRisk);

    // Exercise SUT
    Risk actualRisk = riskFactory.createRisk(unidentifiedRiskDTO);

    // Verify behaviour
    assertThat(actualRisk, is(expectedRisk));
  }
}
