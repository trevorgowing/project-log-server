package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.log.constant.Category.COMMITTED_PEOPLE;
import static com.trevorgowing.projectlog.log.constant.Impact.MODERATE;
import static com.trevorgowing.projectlog.log.constant.LogStatus.NEW;
import static com.trevorgowing.projectlog.log.constant.Probability.POSSIBLE;
import static com.trevorgowing.projectlog.log.constant.RiskResponse.ACCEPT;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class RiskRepositoryTests extends AbstractRepositoryIntegrationTests {

    @Autowired
    private RiskRepository riskRepository;

    @Test
    public void testFindIdentifiedRiskDTOsWithNoExistingRisks_shouldReturnNoIdentifiedRiskDTOs() {
        // Exercise SUT
        List<IdentifiedRiskDTO> actualIdentifiedRiskDTOs = riskRepository.findIdentifiedRiskDTOs();

        // Verify results
        assertThat(actualIdentifiedRiskDTOs, is(empty()));
    }

    @Test
    public void testFindIdentifiedRiskDTOsWithExistingRisks_shouldReturnIdentifiedRiskDTOs() {
        // Set up fixture
        User ownerOne = aUser().email("owner.one@trevorgowing.com").firstName("One").lastName("Owner").build();
        Project projectOne = aProject().code("P1").name("Project One").owner(ownerOne).startDate(LocalDate.now()).build();

        Risk riskOne = aRisk()
                .summary("Risk One Summary")
                .description("Risk One Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(LocalDate.now())
                .project(projectOne)
                .owner(ownerOne)
                .probability(POSSIBLE)
                .riskResponse(ACCEPT)
                .buildAndPersist(entityManager);

        IdentifiedRiskDTO identifiedRiskOneDTO = anIdentifiedRiskDTO()
                .id(riskOne.getId())
                .summary("Risk One Summary")
                .description("Risk One Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(LocalDate.now())
                .project(anIdentifiedProjectDTO().id(projectOne.getId()).code("P1").name("Project One").build())
                .owner(anIdentifiedUserDTO()
                        .id(ownerOne.getId())
                        .email("owner.one@trevorgowing.com")
                        .firstName("One")
                        .lastName("Owner")
                        .build())
                .probability(POSSIBLE)
                .riskResponse(ACCEPT)
                .build();

        User ownerTwo = aUser().email("owner.two@trevorgowing.com").firstName("Two").lastName("Owner").build();
        Project projectTwo = aProject().code("P2").name("Project Two").owner(ownerOne).startDate(LocalDate.now()).build();

        Risk riskTwo = aRisk()
                .summary("Risk Two Summary")
                .description("Risk Two Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(LocalDate.now())
                .project(projectTwo)
                .owner(ownerTwo)
                .probability(POSSIBLE)
                .riskResponse(ACCEPT)
                .buildAndPersist(entityManager);

        IdentifiedRiskDTO identifiedRiskTwoDTO = anIdentifiedRiskDTO()
                .id(riskTwo.getId())
                .summary("Risk Two Summary")
                .description("Risk Two Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(LocalDate.now())
                .project(anIdentifiedProjectDTO().id(projectTwo.getId()).code("P2").name("Project Two").build())
                .owner(anIdentifiedUserDTO()
                        .id(ownerTwo.getId())
                        .email("owner.two@trevorgowing.com")
                        .firstName("Two")
                        .lastName("Owner")
                        .build())
                .probability(POSSIBLE)
                .riskResponse(ACCEPT)
                .build();

        List<IdentifiedRiskDTO> expectedIdentifiedRiskDTOs = asList(identifiedRiskOneDTO, identifiedRiskTwoDTO);

        // Exercise SUT
        List<IdentifiedRiskDTO> actualRisks = riskRepository.findIdentifiedRiskDTOs();

        // Verify results
        assertThat(actualRisks, is(expectedIdentifiedRiskDTOs));
    }

    @Test
    public void testFindIdentifiedRiskDTOByIdWithNoMatchingIssue_shouldReturnNull() {
        // Exercise SUT
        IdentifiedRiskDTO actualIdentifiedRiskDTO = riskRepository.findIdentifiedRiskDTOById(1L);

        // Verify results
        assertThat(actualIdentifiedRiskDTO, is(nullValue()));
    }

    @Test
    public void testFindIdentifiedRiskDTOWithMatchingIssue_shouldReturnIdentifiedRiskDTO() {
        // Set up fixture
        LocalDate date = LocalDate.now();
        User owner = aUser().email("owner@owner.com").password("password").build();
        Project project = aProject().code("P").owner(owner).startDate(date).build();

        Risk risk = aRisk()
                .summary("Summary")
                .description("Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(date)
                .project(project)
                .owner(owner)
                .probability(POSSIBLE)
                .riskResponse(ACCEPT)
                .buildAndPersist(entityManager);

        IdentifiedUserDTO ownerDTO = anIdentifiedUserDTO().id(owner.getId()).email("owner@owner.com").build();
        IdentifiedProjectDTO projectDTO = anIdentifiedProjectDTO().id(project.getId()).code("P").build();

        IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO()
                .id(risk.getId())
                .summary("Summary")
                .description("Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(date)
                .project(projectDTO)
                .owner(ownerDTO)
                .probability(POSSIBLE)
                .riskResponse(ACCEPT)
                .build();

        // Exercise SUT
        IdentifiedRiskDTO actualIdentifiedRiskDTO = riskRepository.findIdentifiedRiskDTOById(risk.getId());

        // Verify results
        assertThat(actualIdentifiedRiskDTO, sameBeanAs(expectedIdentifiedRiskDTO));
    }
}