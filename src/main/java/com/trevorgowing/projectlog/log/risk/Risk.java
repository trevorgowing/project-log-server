package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    private Risk(String summary, String description, Category category, Impact impact, LogStatus status, LocalDate dateClosed, Project project, User owner, Probability probability, RiskResponse riskResponse) {
        super(summary, description, category, impact, status, dateClosed, project, owner);
        this.probability = probability;
        this.riskResponse = riskResponse;
    }

    public static Risk unidentifiedRisk(String summary, String description, Category category, Impact impact,
                                        LogStatus status, LocalDate dateClosed, Project project, User owner,
                                        Probability probability, RiskResponse riskResponse) {
        return new Risk(summary, description, category, impact, status, dateClosed, project, owner, probability,
                riskResponse);
    }

    public static Risk completeRisk(Long id, String summary, String description, Category category, Impact impact,
                                    LogStatus status, LocalDate dateClosed, Project project, User owner,
                                    Probability probability, RiskResponse riskResponse) {
        return new Risk(id, summary, description, category, impact, status, dateClosed, project, owner, probability,
                riskResponse);
    }
}
