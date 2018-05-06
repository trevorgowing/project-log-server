package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.constant.LogConstants.LOGS_URL_PATH;
import static com.trevorgowing.projectlog.log.constant.LogConstants.RISKS_URL_PATH;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(LOGS_URL_PATH + RISKS_URL_PATH)
class RiskController {

  private final RiskFactory riskFactory;
  private final RiskModifier riskModifier;
  private final RiskDTOFactory riskDTOFactory;

  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(CREATED)
  IdentifiedRiskDTO postRisk(@RequestBody UnidentifiedRiskDTO unidentifiedRiskDTO) {
    Risk risk = riskFactory.createRisk(unidentifiedRiskDTO);
    return riskDTOFactory.createIdentifiedRiskDTO(risk);
  }

  @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  IdentifiedRiskDTO putRisk(@RequestBody IdentifiedRiskDTO identifiedRiskDTO) {
    Risk risk = riskModifier.updateRisk(identifiedRiskDTO);
    return riskDTOFactory.createIdentifiedRiskDTO(risk);
  }

  @ExceptionHandler(RiskNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleRiskNotFoundException(
      RiskNotFoundException riskNotFoundException) {
    log.debug(riskNotFoundException.getMessage(), riskNotFoundException);
    return ResponseEntity.status(NOT_FOUND)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, riskNotFoundException.getMessage()));
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleProjectNotFoundException(
      ProjectNotFoundException projectNotFoundException) {
    log.debug(projectNotFoundException.getMessage(), projectNotFoundException);
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, projectNotFoundException.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
      UserNotFoundException userNotFoundException) {
    log.debug(userNotFoundException.getMessage(), userNotFoundException);
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, userNotFoundException.getMessage()));
  }
}
