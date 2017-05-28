package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class UnidentifiedIssueDTO extends LogDTO {

    private static final long serialVersionUID = 4037778154578230261L;

    UnidentifiedIssueDTO(String summary, String description, Category category, Impact impact, LogStatus status,
                         LocalDate dateClosed, IdentifiedProjectDTO project, IdentifiedUserDTO owner) {
        super(summary, description, category, impact, status, dateClosed, project, owner);
    }

    public static UnidentifiedIssueDTO completeUnidentifiedIssueDTO(
            String summary, String description, Category category, Impact impact, LogStatus status,
            LocalDate dateClosed, IdentifiedProjectDTO project, IdentifiedUserDTO owner) {
        return new UnidentifiedIssueDTO(summary, description, category, impact, status, dateClosed,
                project, owner);
    }
}