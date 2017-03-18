package com.trevorgowing.projectlog.common.persistence;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class DateRange {

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDate startDate;

    @Basic
    @Column
    private LocalDate endDate;

    public DateRange() {
    }

    public DateRange(LocalDate startDate) {
        this.startDate = startDate;
    }

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object objectToCompareTo) {
        if (this == objectToCompareTo) return true;
        if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
        DateRange dateRangeToCompareTo = (DateRange) objectToCompareTo;
        return Objects.equals(getStartDate(), dateRangeToCompareTo.getStartDate()) &&
                Objects.equals(getEndDate(), dateRangeToCompareTo.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder("DateRange{");
        toStringBuilder.append("startDate=").append(startDate);
        toStringBuilder.append(", endDate=").append(endDate);
        toStringBuilder.append('}');
        return toStringBuilder.toString();
    }
}

