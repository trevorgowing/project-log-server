package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.ProjectDTOFactory;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.UserDTOFactory;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aCompleteRisk;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class RiskDTOFactoryTests extends AbstractTests {

    @Mock
    private UserDTOFactory userDTOFactory;
    @Mock
    private ProjectDTOFactory projectDTOFactory;

    @InjectMocks
    private RiskDTOFactory riskDTOFactory;

    @Test
    public void testCreateIdentifiedRiskDTO_shouldCreateIdentifiedRiskDTOWithValuesFromGivenRisk() {
        // Set up fixture
        Risk risk = aCompleteRisk().project(aProject().id(1L).build()).owner(aUser().id(1L).build()).build();

        IdentifiedUserDTO identifiedUserDTO = anIdentifiedUserDTO().id(1).build();
        IdentifiedProjectDTO identifiedProjectDTO = anIdentifiedProjectDTO().id(1).build();

        IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO()
                .id(risk.getId())
                .summary(risk.getSummary())
                .description(risk.getDescription())
                .category(risk.getCategory())
                .impact(risk.getImpact())
                .status(risk.getStatus())
                .dateClosed(risk.getDateClosed())
                .project(identifiedProjectDTO)
                .owner(identifiedUserDTO)
                .probability(risk.getProbability())
                .riskResponse(risk.getRiskResponse())
                .build();

        // Set up expectations
        when(userDTOFactory.createIdentifiedUserDTO(risk.getOwner())).thenReturn(identifiedUserDTO);
        when(projectDTOFactory.createIdentifiedProjectDTO(risk.getProject())).thenReturn(identifiedProjectDTO);

        // Exercise SUT
        IdentifiedRiskDTO actualIdentifiedRiskDTO = riskDTOFactory.createIdentifiedRiskDTO(risk);

        // Verify behaviour
        assertThat(actualIdentifiedRiskDTO, sameBeanAs(expectedIdentifiedRiskDTO));
    }
}
