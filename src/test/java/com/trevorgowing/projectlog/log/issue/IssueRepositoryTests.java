package com.trevorgowing.projectlog.log.issue;

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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IssueRepositoryTests extends AbstractRepositoryIntegrationTests {

  @Autowired private IssueRepository issueRepository;

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
    LocalDate date = LocalDate.now();

    User ownerOne =
        aUser().email("owner.one@trevorgowing.com").firstName("One").lastName("Owner").build();
    Project projectOne =
        aProject()
            .code("P1")
            .name("Project One")
            .owner(ownerOne)
            .startDate(LocalDate.now())
            .build();

    Issue issueOne =
        anIssue()
            .summary("Issue One Summary")
            .description("Issue One Description")
            .category(COMMITTED_PEOPLE)
            .impact(MODERATE)
            .status(NEW)
            .dateClosed(date)
            .project(projectOne)
            .owner(ownerOne)
            .buildAndPersist(entityManager);

    IdentifiedIssueDTO identifiedIssueOneDTO =
        anIdentifiedIssueDTO()
            .id(issueOne.getId())
            .summary("Issue One Summary")
            .description("Issue One Description")
            .category(COMMITTED_PEOPLE)
            .impact(MODERATE)
            .status(NEW)
            .dateClosed(date)
            .project(
                anIdentifiedProjectDTO()
                    .id(projectOne.getId())
                    .code("P1")
                    .name("Project One")
                    .build())
            .owner(
                anIdentifiedUserDTO()
                    .id(ownerOne.getId())
                    .email("owner.one@trevorgowing.com")
                    .firstName("One")
                    .lastName("Owner")
                    .build())
            .build();

    User ownerTwo =
        aUser().email("owner.two@trevorgowing.com").firstName("Two").lastName("Owner").build();
    Project projectTwo =
        aProject()
            .code("P2")
            .name("Project Two")
            .owner(ownerOne)
            .startDate(LocalDate.now())
            .build();

    Issue issueTwo =
        anIssue()
            .summary("Issue Two Summary")
            .description("Issue Two Description")
            .category(COMMITTED_PEOPLE)
            .impact(MODERATE)
            .status(NEW)
            .dateClosed(date)
            .project(projectTwo)
            .owner(ownerTwo)
            .buildAndPersist(entityManager);

    IdentifiedIssueDTO identifiedIssueTwoDTO =
        anIdentifiedIssueDTO()
            .id(issueTwo.getId())
            .summary("Issue Two Summary")
            .description("Issue Two Description")
            .category(COMMITTED_PEOPLE)
            .impact(MODERATE)
            .status(NEW)
            .dateClosed(date)
            .project(
                anIdentifiedProjectDTO()
                    .id(projectTwo.getId())
                    .code("P2")
                    .name("Project Two")
                    .build())
            .owner(
                anIdentifiedUserDTO()
                    .id(ownerTwo.getId())
                    .email("owner.two@trevorgowing.com")
                    .firstName("Two")
                    .lastName("Owner")
                    .build())
            .build();

    List<IdentifiedIssueDTO> expectedIdentifiedIssueDTOs =
        asList(identifiedIssueOneDTO, identifiedIssueTwoDTO);

    // Exercise SUT
    List<IdentifiedIssueDTO> actualIdentifiedIssueDTOs = issueRepository.findIdentifiedIssueDTOs();

    // Verify results
    assertThat(actualIdentifiedIssueDTOs, is(expectedIdentifiedIssueDTOs));
  }

  @Test
  public void testFindIdentifiedIssueDTOByIdWithNoMatchingIssue_shouldReturnNull() {
    // Exercise SUT
    IdentifiedIssueDTO actualIdentifiedIssueDTO = issueRepository.findIdentifiedIssueDTOById(1L);

    // Verify results
    assertThat(actualIdentifiedIssueDTO, is(nullValue()));
  }

  @Test
  public void testFindIdentifiedIssueDTOWithMatchingIssue_shouldReturnIdentifiedIssueDTO() {
    // Set up fixture
    LocalDate date = LocalDate.now();
    User owner = aUser().email("owner@owner.com").password("password").build();
    Project project = aProject().code("P").owner(owner).startDate(date).build();

    Issue issue =
        anIssue()
            .summary("Summary")
            .description("Description")
            .category(COMMITTED_PEOPLE)
            .impact(MODERATE)
            .status(NEW)
            .dateClosed(date)
            .project(project)
            .owner(owner)
            .buildAndPersist(entityManager);

    IdentifiedUserDTO ownerDTO =
        anIdentifiedUserDTO().id(owner.getId()).email("owner@owner.com").build();
    IdentifiedProjectDTO projectDTO =
        anIdentifiedProjectDTO().id(project.getId()).code("P").build();

    IdentifiedIssueDTO expectedIdentifiedIssueDTO =
        anIdentifiedIssueDTO()
            .id(issue.getId())
            .summary("Summary")
            .description("Description")
            .category(COMMITTED_PEOPLE)
            .impact(MODERATE)
            .status(NEW)
            .dateClosed(date)
            .project(projectDTO)
            .owner(ownerDTO)
            .build();

    // Exercise SUT
    IdentifiedIssueDTO actualIdentifiedIssueDTO =
        issueRepository.findIdentifiedIssueDTOById(issue.getId());

    // Verify results
    assertThat(actualIdentifiedIssueDTO, samePropertyValuesAs(expectedIdentifiedIssueDTO));
  }
}
