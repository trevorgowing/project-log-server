package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.Log;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.Probability;
import com.trevorgowing.projectlog.log.constant.RiskResponse;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;

import javax.persistence.*;

@Entity
@DiscriminatorValue("risk")
public class Risk extends Log {

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Probability probability;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RiskResponse riskResponse;

    public Risk(String summary, Impact impact, User owner, Project project, Probability probability, RiskResponse riskResponse) {
        super(summary, impact, owner, project);
        this.probability = probability;
        this.riskResponse = riskResponse;
    }

    public Probability getProbability() {
        return probability;
    }

    public void setProbability(Probability probability) {
        this.probability = probability;
    }

    public RiskResponse getRiskResponse() {
        return riskResponse;
    }

    public void setRiskResponse(RiskResponse riskResponse) {
        this.riskResponse = riskResponse;
    }
}
