package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

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
import java.time.LocalDate;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RiskModifierTests extends AbstractTests {

  @Mock private RiskRetriever riskRetriever;
  @Mock private UserRetriever userRetriever;
  @Mock private RiskRepository riskRepository;
  @Mock private ProjectRetriever projectRetriever;

  @InjectMocks private RiskModifier riskModifier;

  @Test(expected = RiskNotFoundException.class)
  public void testUpdateRiskWithNonExistentRisk_shouldThrowRiskNotFoundException() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

    // Set up expectations
    when(riskRetriever.findRisk(1L)).thenThrow(identifiedRiskNotFoundException(1L));

    // Exercise SUT
    riskModifier.updateRisk(identifiedRiskDTO);
  }

  @Test(expected = ProjectNotFoundException.class)
  public void testUpdateRiskWithNonExistentProject_shouldThrowProjectNotFoundException() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO =
        anIdentifiedRiskDTO().id(1L).project(anIdentifiedProjectDTO().id(2L).build()).build();

    Risk risk =
        aRisk().id(1).project(aProject().id(1L).build()).owner(aUser().id(1L).build()).build();

    // Set up expectations
    when(riskRetriever.findRisk(1L)).thenReturn(risk);
    when(projectRetriever.findProject(2L)).thenThrow(identifiedProjectNotFoundException(1L));

    // Exercise SUT
    riskModifier.updateRisk(identifiedRiskDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testUpdateRiskWithNonExistentUser_shouldThrowUserNotFoundException() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO =
        anIdentifiedRiskDTO()
            .id(1L)
            .project(anIdentifiedProjectDTO().id(1L).build())
            .owner(anIdentifiedUserDTO().id(2L).build())
            .build();

    Risk risk =
        aRisk().id(1L).project(aProject().id(1L).build()).owner(aUser().id(1L).build()).build();

    // Set up fixture
    when(riskRetriever.findRisk(1L)).thenReturn(risk);
    when(userRetriever.findUser(2L)).thenThrow(identifiedUserNotFoundException(1L));

    // Exercise SUT
    riskModifier.updateRisk(identifiedRiskDTO);
  }

  @Test
  public void
      testUpdateRiskWithValidRisk_shouldDelegateToRiskRepositoryToSaveRiskAndReturnUpdatedRisk() {
    // Set up fixture
    IdentifiedRiskDTO identifiedRiskDTO =
        anIdentifiedRiskDTO()
            .id(1L)
            .summary("New Summary")
            .description("New Description")
            .category(Category.QUALITY_EXECUTION)
            .impact(Impact.INSIGNIFICANT)
            .status(LogStatus.CLOSED)
            .dateClosed(LocalDate.MAX)
            .project(anIdentifiedProjectDTO().id(2L).build())
            .owner(anIdentifiedUserDTO().id(2L).build())
            .probability(Probability.VERY_UNLIKELY)
            .riskResponse(RiskResponse.MITIGATE)
            .build();

    Risk riskPreUpdate =
        aRisk()
            .id(1L)
            .summary("Old Summary")
            .description("Old Description")
            .category(Category.COMMITTED_PEOPLE)
            .impact(Impact.MODERATE)
            .status(LogStatus.NEW)
            .dateClosed(null)
            .project(aProject().id(1L).build())
            .owner(aUser().id(1L).build())
            .probability(Probability.POSSIBLE)
            .riskResponse(RiskResponse.ACCEPT)
            .build();

    Project project = aProject().id(2L).build();
    User owner = aUser().id(2L).build();

    Risk expectedRisk =
        aRisk()
            .id(1L)
            .summary("New Summary")
            .description("New Description")
            .category(Category.QUALITY_EXECUTION)
            .impact(Impact.INSIGNIFICANT)
            .status(LogStatus.CLOSED)
            .dateClosed(LocalDate.MAX)
            .project(project)
            .owner(owner)
            .probability(Probability.VERY_UNLIKELY)
            .riskResponse(RiskResponse.MITIGATE)
            .build();

    // Set up expectations
    when(riskRetriever.findRisk(1L)).thenReturn(riskPreUpdate);
    when(projectRetriever.findProject(2L)).thenReturn(project);
    when(userRetriever.findUser(2L)).thenReturn(owner);
    when(riskRepository.save(argThat(samePropertyValuesAs(expectedRisk)))).thenReturn(expectedRisk);

    // Exercise SUT
    Risk actualRisk = riskModifier.updateRisk(identifiedRiskDTO);

    // Verify behaviour
    assertThat(actualRisk, is(expectedRisk));
  }
}
