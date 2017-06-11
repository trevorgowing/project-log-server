package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(LogConstants.LOGS_URL_PATH + LogConstants.RISKS_URL_PATH)
class RiskController {

    private final RiskDTOFactory riskDTOFactory;
    private final RiskCRUDService riskCRUDService;

    RiskController(RiskDTOFactory riskDTOFactory, RiskCRUDService riskCRUDService) {
        this.riskDTOFactory = riskDTOFactory;
        this.riskCRUDService = riskCRUDService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    IdentifiedRiskDTO postRisk(@RequestBody UnidentifiedRiskDTO unidentifiedRiskDTO) {
        Risk risk = riskCRUDService.createRisk(unidentifiedRiskDTO);
        return riskDTOFactory.createIdentifiedRiskDTO(risk);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = ProjectNotFoundException.REASON)
    public void handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException) {
        log.warn(projectNotFoundException.getMessage(), projectNotFoundException);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = UserNotFoundException.REASON)
    public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        log.warn(userNotFoundException.getMessage(), userNotFoundException);
    }
}