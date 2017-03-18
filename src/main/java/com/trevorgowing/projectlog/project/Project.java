
package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;
import com.trevorgowing.projectlog.common.persistence.DateRange;
import com.trevorgowing.projectlog.common.persistence.HasDateRange;
import com.trevorgowing.projectlog.common.persistence.ValidatedDateRange;
import com.trevorgowing.projectlog.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Getter
@Setter
public class Project extends AbstractAuditable<User, Long> implements HasDateRange {

    private static final long serialVersionUID = -2208694199836258417L;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String code;

    private String name;

    @ManyToOne(optional = false)
    private User owner;

    @Basic(optional = false)
    @Column(nullable = false)
    @Embedded
    @ValidatedDateRange
    private DateRange dateRange;

    @SuppressWarnings("unused")
    public Project(String code, User owner) {
        this.code = code;
        this.owner = owner;
    }

    @Override
    public LocalDate getStartDate() {
        return dateRange == null ? null : dateRange.getStartDate();
    }

    @Override
    public void setStartDate(LocalDate startDate) {
        ensureDateRange();
        dateRange.setStartDate(startDate);
    }

    @Override
    public LocalDate getEndDate() {
        return dateRange == null ? null : dateRange.getEndDate();
    }

    @Override
    public void setEndDate(LocalDate endDate) {
        ensureDateRange();
        dateRange.setEndDate(endDate);
    }

    @Override
    public void ensureDateRange() {
        dateRange = dateRange == null ? new DateRange() : dateRange;
    }
}
