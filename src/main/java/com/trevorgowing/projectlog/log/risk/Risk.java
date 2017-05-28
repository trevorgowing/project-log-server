package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.*;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("risk")
@NoArgsConstructor
@Getter
@Setter
public class Risk extends Log {

    private static final long serialVersionUID = 8370923292436167347L;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private Probability probability;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private RiskResponse riskResponse;

    private Risk(Long id, String summary, String description, Category category, Impact impact, LogStatus status,
                LocalDate dateClosed, Project project, User owner, Probability probability, RiskResponse riskResponse) {
        super(id, summary, description, category, impact, status, dateClosed, project, owner);
        this.probability = probability;
        this.riskResponse = riskResponse;
    }

    public static Risk completeRisk(Long id, String summary, String description, Category category, Impact impact,
                                    LogStatus status, LocalDate dateClosed, Project project, User owner,
                                    Probability probability, RiskResponse riskResponse) {
        return new Risk(id, summary, description, category, impact, status, dateClosed, project, owner, probability,
                riskResponse);
    }
}
