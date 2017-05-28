package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class LogDTO implements Serializable {

    private static final long serialVersionUID = 2791426410861030730L;

    protected String summary;
    protected String description;
    protected Category category;
    protected Impact impact;
    protected LogStatus status;
    protected LocalDate dateClosed;
    protected IdentifiedProjectDTO project;
    protected IdentifiedUserDTO owner;

    @Override
    public boolean equals(Object objectToCompareTo) {
        if (this == objectToCompareTo) return true;
        if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
        LogDTO logDTOToCompareTo = (LogDTO) objectToCompareTo;
        return Objects.equals(summary, logDTOToCompareTo.summary) &&
                impact == logDTOToCompareTo.impact &&
                status == logDTOToCompareTo.status &&
                Objects.equals(dateClosed, logDTOToCompareTo.dateClosed) &&
                Objects.equals(project, logDTOToCompareTo.project) &&
                Objects.equals(owner, logDTOToCompareTo.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(summary, impact, status, dateClosed, project, owner);
    }

    @Override
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
        toStringBuilder.append("{ summary='").append(summary).append('\'');
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