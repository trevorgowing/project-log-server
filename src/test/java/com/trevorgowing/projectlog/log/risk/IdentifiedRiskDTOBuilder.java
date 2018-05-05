package com.trevorgowing.projectlog.log.risk;

import static com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO.completeIdentifiedRiskDTO;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.log.AbstractLogDTOBuilder;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;

public class IdentifiedRiskDTOBuilder extends AbstractLogDTOBuilder<IdentifiedRiskDTOBuilder>
    implements DomainObjectBuilder<IdentifiedRiskDTO> {

  private long id;
  private Probability probability;
  private RiskResponse riskResponse;

  public static IdentifiedRiskDTOBuilder anIdentifiedRiskDTO() {
    return new IdentifiedRiskDTOBuilder();
  }

  @Override
  public IdentifiedRiskDTO build() {
    return completeIdentifiedRiskDTO(
        id,
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

  public IdentifiedRiskDTOBuilder id(long id) {
    this.id = id;
    return this;
  }

  public IdentifiedRiskDTOBuilder probability(Probability probability) {
    this.probability = probability;
    return this;
  }

  public IdentifiedRiskDTOBuilder riskResponse(RiskResponse riskResponse) {
    this.riskResponse = riskResponse;
    return this;
  }
}
