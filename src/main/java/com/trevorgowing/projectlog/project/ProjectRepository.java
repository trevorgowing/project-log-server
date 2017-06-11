package com.trevorgowing.projectlog.project;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {

    String QUERY_FIND_IDENTIFIED_PROJECT_DTOS =
            "SELECT NEW com.trevorgowing.projectlog.project.IdentifiedProjectDTO(" +
            "p.id, p.code, p.name, p.dateRange.startDate, p.dateRange.endDate, p.owner.id, p.owner.email, " +
            "p.owner.firstName, p.owner.lastName) " +
            "FROM Project p";

    @Query(QUERY_FIND_IDENTIFIED_PROJECT_DTOS)
    List<IdentifiedProjectDTO> findIdentifiedProjectDTOs();

    String QUERY_FIND_IDENTIFIED_PROJECT_DTO_BY_ID =
            "SELECT NEW com.trevorgowing.projectlog.project.IdentifiedProjectDTO(" +
            "p.id, p.code, p.name, p.dateRange.startDate, p.dateRange.endDate, p.owner.id, p.owner.email, " +
            "p.owner.firstName, p.owner.lastName) " +
            "FROM Project p " +
            "WHERE p.id = ?1";

    @Query(QUERY_FIND_IDENTIFIED_PROJECT_DTO_BY_ID)
    IdentifiedProjectDTO findIdentifiedProjectDTOById(long projectId);
}
