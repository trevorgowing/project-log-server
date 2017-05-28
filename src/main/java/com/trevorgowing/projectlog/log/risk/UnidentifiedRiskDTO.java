package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.constant.*;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class UnidentifiedRiskDTO extends LogDTO {

    private static final long serialVersionUID = 8964604470153989245L;

    protected Probability probability;
    protected RiskResponse riskResponse;

    UnidentifiedRiskDTO(String summary, String description, Category category, Impact impact, LogStatus status,
                        LocalDate dateClosed, IdentifiedProjectDTO project, IdentifiedUserDTO owner,
                        Probability probability, RiskResponse riskResponse) {
        super(summary, description, category, impact, status, dateClosed, project, owner);
        this.probability = probability;
        this.riskResponse = riskResponse;
    }

    public static UnidentifiedRiskDTO completeUnidentifiedRiskDTO(
            String summary, String description, Category category, Impact impact, LogStatus status,
            LocalDate dateClosed, IdentifiedProjectDTO project, IdentifiedUserDTO owner, Probability probability,
            RiskResponse riskResponse) {
        return new UnidentifiedRiskDTO(summary, description, category, impact, status, dateClosed,
                project, owner, probability, riskResponse);
    }

    @Override
    public boolean equals(Object objectToCompareTo) {
        if (this == objectToCompareTo) return true;
        if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
        if (!super.equals(objectToCompareTo)) return false;
        UnidentifiedRiskDTO unidentifiedRiskDTOToCompareTo = (UnidentifiedRiskDTO) objectToCompareTo;
        return probability == unidentifiedRiskDTOToCompareTo.probability &&
                riskResponse == unidentifiedRiskDTOToCompareTo.riskResponse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), probability, riskResponse);
    }

    @Override
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
        toStringBuilder.append("{ probability=").append(probability);
        toStringBuilder.append(", riskResponse=").append(riskResponse);
        toStringBuilder.append(", summary='").append(summary).append('\'');
        toStringBuilder.append(", description='").append(description).append('\'');
        toStringBuilder.append(", category=").append(category);
        toStringBuilder.append(", impact=").append(impact);
        toStringBuilder.append(", status=").append(status);
        toStringBuilder.append(", dateClosed=").append(dateClosed);
        toStringBuilder.append(", project=").append(project);
        toStringBuilder.append(", owner=").append(owner);
        toStringBuilder.append('}');
        return toStringBuilder.toString();
    }
}