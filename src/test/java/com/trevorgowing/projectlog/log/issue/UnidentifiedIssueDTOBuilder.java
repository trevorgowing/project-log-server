package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.log.AbstractLogDTOBuilder;

import static com.trevorgowing.projectlog.log.issue.UnidentifiedIssueDTO.completeUnidentifiedIssueDTO;

public class UnidentifiedIssueDTOBuilder extends AbstractLogDTOBuilder<UnidentifiedIssueDTOBuilder>
        implements DomainObjectBuilder<UnidentifiedIssueDTO> {

    public static UnidentifiedIssueDTOBuilder anUnidentifiedIssueDTO() {
        return new UnidentifiedIssueDTOBuilder();
    }

    @Override
    public UnidentifiedIssueDTO build() {
        return completeUnidentifiedIssueDTO(summary, description, category, impact, status, dateClosed,
                project, owner);
    }
}