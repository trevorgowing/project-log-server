package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.constant.*;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTO.minimalIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.minimalIdentifiedUserDTO;

@NoArgsConstructor
@Getter
@Setter
public class IdentifiedRiskDTO extends UnidentifiedRiskDTO {

    private static final long serialVersionUID = -6676273201602521461L;

    private long id;

    private IdentifiedRiskDTO(long id, String summary, String description, Category category, Impact impact,
                              LogStatus status, LocalDate dateClosed, IdentifiedProjectDTO project,
                              IdentifiedUserDTO owner, Probability probability, RiskResponse riskResponse) {
        super(summary, description, category, impact, status, dateClosed, project, owner, probability, riskResponse);
        this.id = id;
    }

    /**
     * Used in constructor queries {@link RiskRepository#findIdentifiedRiskDTOs()}.
     */
    @SuppressWarnings("unused")
    public IdentifiedRiskDTO(long id, String summary, String description, Category category, Impact impact,
                             LogStatus status, LocalDate dateClosed, long projectId, String projectCode,
                             String projectName, long ownerId, String ownerEmail, String ownerFirstName,
                             String ownerLastName, Probability probability, RiskResponse riskResponse) {
        super(summary, description, category, impact, status, dateClosed,
                minimalIdentifiedProjectDTO(projectId, projectCode, projectName),
                minimalIdentifiedUserDTO(ownerId, ownerEmail, ownerFirstName, ownerLastName),
                probability, riskResponse);
        this.id = id;
    }

    public static IdentifiedRiskDTO completeIdentifiedRiskDTO(
            long id, String summary, String description, Category category, Impact impact, LogStatus status,
            LocalDate dateClosed, IdentifiedProjectDTO project, IdentifiedUserDTO owner, Probability probability,
            RiskResponse riskResponse) {
        return new IdentifiedRiskDTO(id, summary, description, category, impact, status, dateClosed,
                project, owner, probability, riskResponse);
    }

    @Override
    public boolean equals(Object objectToCompareTo) {
        if (this == objectToCompareTo) return true;
        if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
        if (!super.equals(objectToCompareTo)) return false;
        IdentifiedRiskDTO identifiedRiskDTOToCompareTo = (IdentifiedRiskDTO) objectToCompareTo;
        return id == identifiedRiskDTOToCompareTo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
        toStringBuilder.append("{ id=").append(id);
        toStringBuilder.append(", probability=").append(probability);
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
