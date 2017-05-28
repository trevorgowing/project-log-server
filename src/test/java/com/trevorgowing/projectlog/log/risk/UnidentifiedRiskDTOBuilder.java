package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.log.AbstractLogDTOBuilder;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;

import static com.trevorgowing.projectlog.log.risk.UnidentifiedRiskDTO.completeUnidentifiedRiskDTO;

public class UnidentifiedRiskDTOBuilder extends AbstractLogDTOBuilder<UnidentifiedRiskDTOBuilder>
        implements DomainObjectBuilder<UnidentifiedRiskDTO> {

    private Probability probability;
    private RiskResponse riskResponse;

    public static UnidentifiedRiskDTOBuilder anUnidentifiedRiskDTO() {
        return new UnidentifiedRiskDTOBuilder();
    }

    @Override
    public UnidentifiedRiskDTO build() {
        return completeUnidentifiedRiskDTO(summary, description, category, impact, status, dateClosed,
                project, owner, probability, riskResponse);
    }

    public UnidentifiedRiskDTOBuilder probability(Probability probability) {
        this.probability = probability;
        return this;
    }

    public UnidentifiedRiskDTOBuilder riskResponse(RiskResponse riskResponse) {
        this.riskResponse = riskResponse;
        return this;
    }
}