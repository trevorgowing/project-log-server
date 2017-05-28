package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.trevorgowing.projectlog.log.constant.Category.COMMITTED_PEOPLE;
import static com.trevorgowing.projectlog.log.constant.Impact.MODERATE;
import static com.trevorgowing.projectlog.log.constant.LogStatus.NEW;
import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTOBuilder.anIdentifiedIssueDTO;
import static com.trevorgowing.projectlog.log.issue.IssueBuilder.anIssue;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class IssueRepositoryTests extends AbstractRepositoryIntegrationTests {

    @Autowired
    private IssueRepository issueRepository;

    @Test
    public void testFindIdentifiedIssueDTOsWithNoExistingIssues_shouldReturnNoIdentifiedIssuesDTOs() {
        // Exercise SUT
        List<IdentifiedIssueDTO> actualIdentifiedIssueDTOs = issueRepository.findIdentifiedIssueDTOs();

        // Verify results
        assertThat(actualIdentifiedIssueDTOs, is(empty()));
    }

    @Test
    public void testFindIdentifiedIssueDTOsWithExistingIssues_shouldReturnIdentifiedIssueDTOs() {
        // Set up fixture
        User ownerOne = aUser().email("owner.one@trevorgowing.com").firstName("One").lastName("Owner").build();
        Project projectOne = aProject().code("P1").name("Project One").owner(ownerOne).startDate(LocalDate.now())
                .build();

        Issue issueOne = anIssue()
                .summary("Issue One Summary")
                .description("Issue One Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(LocalDate.now())
                .project(projectOne)
                .owner(ownerOne)
                .buildAndPersist(entityManager);

        IdentifiedIssueDTO identifiedIssueOneDTO = anIdentifiedIssueDTO()
                .id(issueOne.getId())
                .summary("Issue One Summary")
                .description("Issue One Description")
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
                .build();

        User ownerTwo = aUser().email("owner.two@trevorgowing.com").firstName("Two").lastName("Owner").build();
        Project projectTwo = aProject().code("P2").name("Project Two").owner(ownerOne).startDate(LocalDate.now()).
                build();

        Issue issueTwo = anIssue()
                .summary("Issue Two Summary")
                .description("Issue Two Description")
                .category(COMMITTED_PEOPLE)
                .impact(MODERATE)
                .status(NEW)
                .dateClosed(LocalDate.now())
                .project(projectTwo)
                .owner(ownerTwo)
                .buildAndPersist(entityManager);

        IdentifiedIssueDTO identifiedIssueTwoDTO = anIdentifiedIssueDTO()
                .id(issueTwo.getId())
                .summary("Issue Two Summary")
                .description("Issue Two Description")
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
                .build();

        List<IdentifiedIssueDTO> expectedIdentifiedIssueDTOs = asList(identifiedIssueOneDTO, identifiedIssueTwoDTO);

        // Exercise SUT
        List<IdentifiedIssueDTO> actualIdentifiedIssueDTOs = issueRepository.findIdentifiedIssueDTOs();

        // Verify results
        assertThat(actualIdentifiedIssueDTOs, is(expectedIdentifiedIssueDTOs));
    }
}