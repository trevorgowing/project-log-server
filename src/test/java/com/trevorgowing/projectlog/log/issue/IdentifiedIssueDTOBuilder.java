package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;
import com.trevorgowing.projectlog.log.AbstractLogDTOBuilder;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO.completeIdentifiedIssueDTO;

public class IdentifiedIssueDTOBuilder extends AbstractLogDTOBuilder<IdentifiedIssueDTOBuilder>
        implements DomainObjectBuilder<IdentifiedIssueDTO> {

    private long id;

    public static IdentifiedIssueDTOBuilder anIdentifiedIssueDTO() {
        return new IdentifiedIssueDTOBuilder();
    }

    @Override
    public IdentifiedIssueDTO build() {
        return completeIdentifiedIssueDTO(id, summary, description, category, impact, status, dateClosed,
                project, owner);
    }

    public IdentifiedIssueDTOBuilder id(long id) {
        this.id = id;
        return this;
    }
}
