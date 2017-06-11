package com.trevorgowing.projectlog.common.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class DateRange implements Serializable {

    private static final long serialVersionUID = -6712791788356051692L;

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @SuppressWarnings("unused")
    public DateRange(LocalDate startDate) {
        this.startDate = startDate;
    }
}

