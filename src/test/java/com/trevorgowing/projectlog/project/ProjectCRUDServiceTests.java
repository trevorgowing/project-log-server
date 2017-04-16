package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ProjectCRUDServiceTests extends AbstractTests {

    private static final long IRRELEVANT_PROJECT_ID = 1L;
    private static final String IRRELEVANT_CODE = "irrelevant.project.code";
    private static final String IRRELEVANT_NAME = "irrelevant.project.name";
    private static final IdentifiedUserDTO IRRELEVANT_OWNER = anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectCRUDService projectCRUDService;

    @Test
    public void testGetIdentifiedProjectDTOs_shouldDelegateToProjectRepositoryAndReturnIdentifiedProjectDTOs() {
        // Set up fixture
        IdentifiedProjectDTO identifiedProjectOneDTO = anIdentifiedProjectDTO()
                .id(IRRELEVANT_PROJECT_ID)
                .code(IRRELEVANT_CODE)
                .name(IRRELEVANT_NAME)
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
}