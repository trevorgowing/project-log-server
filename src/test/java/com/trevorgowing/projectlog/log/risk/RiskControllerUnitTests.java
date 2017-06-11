package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.UrlStringBuilder.basedUrlBuilder;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTOBuilder.anIdentifiedRiskDTO;
import static com.trevorgowing.projectlog.log.risk.RiskBuilder.aRisk;
import static com.trevorgowing.projectlog.log.risk.UnidentifiedRiskDTOBuilder.anUnidentifiedRiskDTO;
import static com.trevorgowing.projectlog.project.IdentifiedProjectDTOBuilder.anIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class RiskControllerUnitTests extends AbstractControllerUnitTests {

    @Mock
    private RiskDTOFactory riskDTOFactory;
    @Mock
    private RiskCRUDService riskCRUDService;

    @InjectMocks
    private RiskController riskController;

    @Override
    protected Object getController() {
        return riskController;
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testPostRiskWithNonExistentProject_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO()
                .project(anIdentifiedProjectDTO().id(1L).build())
                .build();

        // Set up expectations
        when(riskCRUDService.createRisk(unidentifiedRiskDTO)).thenThrow(identifiedProjectNotFoundException(1L));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedRiskDTO))
        .when()
                .post(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(LogConstants.RISKS_URL_PATH).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        riskController.postRisk(unidentifiedRiskDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void testPostRiskWithNonExistentOwner_shouldRespondWithStatusConflict() throws Exception {
        // Set up fixture
        UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO()
                .owner(anIdentifiedUserDTO().id(1L).build())
                .build();

        // Set up expectations
        when(riskCRUDService.createRisk(unidentifiedRiskDTO)).thenThrow(identifiedUserNotFoundException(1L));

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedRiskDTO))
        .when()
                .post(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(LogConstants.RISKS_URL_PATH).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        riskController.postRisk(unidentifiedRiskDTO);
    }

    @Test
    public void testPostRiskWithValidRisk_shouldRespondWithStatusCreatedAndReturnIdentifiedRiskDTO() throws Exception {
        // Set up fixture
        UnidentifiedRiskDTO unidentifiedRiskDTO = anUnidentifiedRiskDTO().build();

        Risk risk = aRisk().build();

        IdentifiedRiskDTO expectedIdentifiedRiskDTO = anIdentifiedRiskDTO().build();

        // Set up expectations
        when(riskCRUDService.createRisk(unidentifiedRiskDTO)).thenReturn(risk);
        when(riskDTOFactory.createIdentifiedRiskDTO(risk)).thenReturn(expectedIdentifiedRiskDTO);

        // Exercise SUT
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(convertToJSON(unidentifiedRiskDTO))
        .when()
                .post(basedUrlBuilder(LogConstants.LOGS_URL_PATH).appendPath(LogConstants.RISKS_URL_PATH).toString())
        .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedIdentifiedRiskDTO)));

        IdentifiedRiskDTO actualIdentifiedRiskDTO = riskController.postRisk(unidentifiedRiskDTO);

        // Verify behaviour
        assertThat(actualIdentifiedRiskDTO, is(expectedIdentifiedRiskDTO));
    }
}