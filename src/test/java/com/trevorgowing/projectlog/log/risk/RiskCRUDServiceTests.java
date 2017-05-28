package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class RiskCRUDServiceTests extends AbstractTests {

    @Mock
    private RiskRepository riskRepository;

    @InjectMocks
    private RiskCRUDService riskCRUDService;

    @Test
    public void testGetIdentifiedRiskDTOs_shouldDelegateToRiskRepositoryAndReturnIdentifiedRiskDTOs() {
        // Set up fixture
        List<IdentifiedRiskDTO> expectedIdentifiedRiskDTOs = asList(anIdentifiedRiskDTO().id(1).summary("Risk One").build(),
                anIdentifiedRiskDTO().id(2).summary("Risk Two").build());

        // Set up expectations
        when(riskRepository.findIdentifiedRiskDTOs()).thenReturn(expectedIdentifiedRiskDTOs);

        // Exercise SUT
        List<IdentifiedRiskDTO> actualIdentifiedRiskDTOs = riskCRUDService.getIdentifiedRiskDTOs();

        // Verify behaviour
        assertThat(actualIdentifiedRiskDTOs, is(expectedIdentifiedRiskDTOs));
    }
}