package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.log.LogDTO;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.log.risk.RiskNotFoundException.identifiedRiskNotFoundException;
import static com.trevorgowing.projectlog.log.risk.UnidentifiedRiskDTOBuilder.anUnidentifiedRiskDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class RiskCRUDServiceTests extends AbstractTests {

    @Mock
    private UserRetriever userRetriever;
    @Mock
    private RiskRepository riskRepository;
    @Mock
    private ProjectRetriever projectRetriever;

    @InjectMocks
    private RiskCRUDService riskCRUDService;

    @Test(expected = RiskNotFoundException.class)
    public void testFindRiskWithNonExistentRisk_shouldThrowRiskNotFoundException() {
        // Set up expectations
        when(riskRepository.findOne(1L)).thenThrow(identifiedRiskNotFoundException(1L));

        // Exercise SUT
        riskCRUDService.findRisk(1L);
    }

    @Test
    public void testFindRiskWithExistingRisk_shouldReturnRisk() {
        // Set up fixture
        Risk expectedRisk = aRisk().id(1L).build();

        // Set up expectations
        when(riskRepository.findOne(1L)).thenReturn(expectedRisk);

        // Exercise SUT
        Risk actualRisk = riskCRUDService.findRisk(1L);

        // Verify behaviour
        assertThat(actualRisk, is(expectedRisk));
    }

    @Test
    public void testGetLogDTOs_shouldDelegateToRiskRepositoryAndReturnLogDTOs() {
        // Set up fixture
        List<IdentifiedRiskDTO> identifiedRiskDTOs = asList(
                anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
                anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

        List<LogDTO> expectedLogDTOs = new ArrayList<>(identifiedRiskDTOs);

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(identifiedRiskDTOs);

        // Exercise SUT
        List<LogDTO> actualLogDTOs = riskCRUDService.getLogDTOs();

        // Verify behaviour
        assertThat(actualLogDTOs, is(expectedLogDTOs));
    }

    @Test(expected = RiskNotFoundException.class)
    public void testGetLogDTOByIdWithNoMatchingRisk_shouldThrowIdentifiedRiskNotFoundException() {
        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOById(1L)).thenReturn(null);

        // Exercise SUT
        riskCRUDService.getLogDTOById(1L);
    }

    @Test
    public void testGetLogDTOByIdWithMatchingRisk_shouldDelegateToIssueRepositoryAndReturnIdentifiedIssueDTO() {
        // Set up fixture
        IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOById(1L)).thenReturn(expectedIdentifiedRiskDTO);

        // Exercise SUT
        LogDTO actualLogDTO = riskCRUDService.getLogDTOById(1L);

        // Verify behaviour
        assertThat(actualLogDTO, is(expectedIdentifiedRiskDTO));
    }

    @Test
    public void testGetIdentifiedRiskDTOs_shouldDelegateToRiskRepositoryAndReturnIdentifiedRiskDTOs() {
        // Set up fixture
        List<IdentifiedRiskDTO> expectedIdentifiedRiskDTOs = asList(
                anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
                anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(expectedIdentifiedRiskDTOs);

        // Exercise SUT
        List<IdentifiedRiskDTO> actualIdentifiedRiskDTOs = riskCRUDService.getIdentifiedRiskDTOs();

        // Verify behaviour
        assertThat(actualIdentifiedRiskDTOs, is(expectedIdentifiedRiskDTOs));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testCreateRiskWithNonExistentProject_shouldThrowProjectNotFoundException() {
        // Set up fixture
        UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO()
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
        riskCRUDService.createRisk(unidentifiedRiskDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateRiskWithNonExistentOwner_shouldThrowUserNotFoundException() {
        // Set up fixture
        UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO()
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
        riskCRUDService.createRisk(unidentifiedRiskDTO);
    }

    @Test
    public void testCreateRiskWithValidRisk_shouldDelegateToRiskRepositoryToSaveRiskAndReturnManagedRisk() {
        // Set up fixture
        UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO()
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

        Risk unidentifiedRisk = aRisk()
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

        Risk expectedRisk = aRisk()
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
        when(riskRepository.save(argThat(samePropertyValuesAs(unidentifiedRisk)))).thenReturn(expectedRisk);

        // Exercise SUT
        Risk actualRisk = riskCRUDService.createRisk(unidentifiedRiskDTO);

        // Verify behaviour
        assertThat(actualRisk, is(expectedRisk));
    }

    @Test(expected = RiskNotFoundException.class)
    public void testUpdateRiskWithNonExistentRisk_shouldThrowRiskNotFoundException() {
        // Set up fixture
        IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L).build();

        // Set up expectations
        when(riskRepository.findOne(1L)).thenReturn(null);

        // Exercise SUT
        riskCRUDService.updateRisk(identifiedRiskDTO);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testUpdateRiskWithNonExistentProject_shouldThrowProjectNotFoundException() {
        // Set up fixture
        IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L)
                .project(anIdentifiedProjectDTO().id(2L).build()).build();

        Risk risk = aRisk().id(1)
                .project(aProject().id(1L).build())
                .owner(aUser().id(1L).build())
                .build();

        // Set up expectations
        when(riskRepository.findOne(1L)).thenReturn(risk);
        when(projectRetriever.findProject(2L)).thenThrow(identifiedProjectNotFoundException(1L));

        // Exercise SUT
        riskCRUDService.updateRisk(identifiedRiskDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateRiskWithNonExistentUser_shouldThrowUserNotFoundException() {
        // Set up fixture
        IdentifiedRiskDTO identifiedRiskDTO = anIdentifiedRiskDTO().id(1L)
                .project(anIdentifiedProjectDTO().id(1L).build())
                .owner(anIdentifiedUserDTO().id(2L).build())
                .build();

        Risk risk = aRisk().id(1L)
                .project(aProject().id(1L).build())
                .owner(aUser().id(1L).build())
                .build();

        // Set up fixture
        when(riskRepository.findOne(1L)).thenReturn(risk);
        when(userRetriever.findUser(2L)).thenThrow(identifiedUserNotFoundException(1L));

        // Exercise SUT
        riskCRUDService.updateRisk(identifiedRiskDTO);
    }

    @Test
    public void testUpdateRiskWithValidRisk_shouldDelegateToRiskRepositoryToSaveRiskAndReturnUpdatedRisk() {
        // Set up fixture
        IdentifiedRiskDTO identifiedRiskDTO =anIdentifiedRiskDTO()
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

        Risk riskPreUpdate = aRisk()
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

        Risk expectedRisk = aRisk()
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
        when(riskRepository.findOne(1L)).thenReturn(riskPreUpdate);
        when(projectRetriever.findProject(2L)).thenReturn(project);
        when(userRetriever.findUser(2L)).thenReturn(owner);
        when(riskRepository.save(argThat(samePropertyValuesAs(expectedRisk)))).thenReturn(expectedRisk);

        // Exercise SUT
        Risk actualRisk = riskCRUDService.updateRisk(identifiedRiskDTO);

        // Verify behaviour
        assertThat(actualRisk, is(expectedRisk));
    }
}