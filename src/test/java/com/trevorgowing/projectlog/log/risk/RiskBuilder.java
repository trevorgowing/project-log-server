package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.common.builders.AbstractEntityBuilder;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import com.trevorgowing.projectlog.log.constant.*;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.user.User;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.log.constant.Impact.MODERATE;
import static com.trevorgowing.projectlog.log.constant.LogStatus.NEW;
import static com.trevorgowing.projectlog.log.constant.Probability.POSSIBLE;
import static com.trevorgowing.projectlog.log.constant.RiskResponse.ACCEPT;
import static com.trevorgowing.projectlog.log.risk.Risk.completeRisk;
import static com.trevorgowing.projectlog.project.ProjectBuilder.aProject;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;

public class RiskBuilder extends AbstractEntityBuilder<Risk> {

    private Long id;
    private String summary = "Default Risk Summary";
    private String description;
    private Category category;
    private Impact impact = MODERATE;
    private LogStatus status = NEW;
    private LocalDate dateClosed;
    private Project project = aProject().build();
    private User owner = aUser().build();
    private Probability probability = POSSIBLE;
    private RiskResponse riskResponse = ACCEPT;

    public static RiskBuilder aRisk() {
        return new RiskBuilder();
    }

    @Override
    public Risk build() {
        return completeRisk(id, summary, description, category, impact, status, dateClosed, project, owner, probability, 
                riskResponse);
    }

    @Override
    public AbstractEntityPersister<Risk> getPersister() {
        return new RiskPersister();
    }

    public RiskBuilder id(long id) {
        this.id = id;
        return this;
    }

    public RiskBuilder summary(String summary) {
        this.summary = summary;
        return this;
    }

    public RiskBuilder description(String description) {
        this.description = description;
        return this;
    }

    public RiskBuilder category(Category category) {
        this.category = category;
        return this;
    }

    public RiskBuilder impact(Impact impact) {
        this.impact = impact;
        return this;
    }

    public RiskBuilder status(LogStatus status) {
        this.status = status;
        return this;
    }

    public RiskBuilder dateClosed(LocalDate dateClosed) {
        this.dateClosed = dateClosed;
        return this;
    }

    public RiskBuilder project(Project project) {
        this.project = project;
        return this;
    }

    public RiskBuilder owner(User owner) {
        this.owner = owner;
        return this;
    }

    public RiskBuilder probability(Probability probability) {
        this.probability = probability;
        return this;
    }

    public RiskBuilder riskResponse(RiskResponse riskResponse) {
        this.riskResponse = riskResponse;
        return this;
    }
}