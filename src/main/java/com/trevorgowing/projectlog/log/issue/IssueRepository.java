package com.trevorgowing.projectlog.log.issue;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

  String QUERY_FIND_IDENTIFIED_ISSUE_DTOS =
      "SELECT NEW com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO(i.id, i.summary, i.description, "
          + "i.category, i.impact, i.status, i.dateClosed, i.project.id, i.project.code, i.project.name, i.owner.id,  "
          + "i.owner.email, i.owner.firstName, i.owner.lastName) "
          + "FROM Issue i ";

  @Query(QUERY_FIND_IDENTIFIED_ISSUE_DTOS)
  List<IdentifiedIssueDTO> findIdentifiedIssueDTOs();

  String QUERY_FIND_IDENTIFIED_ISSUE_DTO_BY_ID =
      "SELECT NEW com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO(i.id, i.summary, i.description, "
          + "i.category, i.impact, i.status, i.dateClosed, i.project.id, i.project.code, i.project.name, i.owner.id,  "
          + "i.owner.email, i.owner.firstName, i.owner.lastName) "
          + "FROM Issue i "
          + "WHERE i.id = ?1";

  @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
  @Query(QUERY_FIND_IDENTIFIED_ISSUE_DTO_BY_ID)
  IdentifiedIssueDTO findIdentifiedIssueDTOById(long issueId);
}
