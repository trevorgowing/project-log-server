package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class UnidentifiedRiskDTO extends LogDTO {

  private static final long serialVersionUID = 8964604470153989245L;

  protected Probability probability;
  protected RiskResponse riskResponse;

  UnidentifiedRiskDTO(
      String summary,
      String description,
      Category category,
      Impact impact,
      LogStatus status,
      LocalDate dateClosed,
      IdentifiedProjectDTO project,
      IdentifiedUserDTO owner,
      Probability probability,
      RiskResponse riskResponse) {
    super(summary, description, category, impact, status, dateClosed, project, owner);
    this.probability = probability;
    this.riskResponse = riskResponse;
  }

  public static UnidentifiedRiskDTO completeUnidentifiedRiskDTO(
      String summary,
      String description,
      Category category,
      Impact impact,
      LogStatus status,
      LocalDate dateClosed,
      IdentifiedProjectDTO project,
      IdentifiedUserDTO owner,
      Probability probability,
      RiskResponse riskResponse) {
    return new UnidentifiedRiskDTO(
        summary,
        description,
        category,
        impact,
        status,
        dateClosed,
        project,
        owner,
        probability,
        riskResponse);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    if (!super.equals(objectToCompareTo)) return false;
    UnidentifiedRiskDTO unidentifiedRiskDTOToCompareTo = (UnidentifiedRiskDTO) objectToCompareTo;
    return probability == unidentifiedRiskDTOToCompareTo.probability
        && riskResponse == unidentifiedRiskDTOToCompareTo.riskResponse;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), probability, riskResponse);
  }
}
