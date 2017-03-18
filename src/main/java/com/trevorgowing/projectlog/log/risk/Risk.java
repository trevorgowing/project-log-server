package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("risk")
@NoArgsConstructor
@Getter
@Setter
public class Risk extends Log {

    private static final long serialVersionUID = 8370923292436167347L;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Probability probability;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RiskResponse riskResponse;

    @SuppressWarnings("unused")
    public Risk(String summary, Impact impact, User owner, Project project, Probability probability,
                RiskResponse riskResponse) {
        super(summary, impact, owner, project);
        this.probability = probability;
        this.riskResponse = riskResponse;
    }
}
