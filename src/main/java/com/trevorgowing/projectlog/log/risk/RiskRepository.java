package com.trevorgowing.projectlog.log.risk;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

interface RiskRepository extends PagingAndSortingRepository<Risk, Long> {

    String QUERY_FIND_IDENTIFIED_RISK_DTOS =
            "SELECT NEW com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO(r.id, r.summary, r.description, " +
            "r.category, r.impact, r.status, r.dateClosed, r.project.id, r.project.code, r.project.name, r.owner.id,  " +
            "r.owner.email, r.owner.firstName, r.owner.lastName, r.probability, r.riskResponse) " +
            "FROM Risk r ";

    @Query(QUERY_FIND_IDENTIFIED_RISK_DTOS)
    List<IdentifiedRiskDTO> findIdentifiedRiskDTOs();

    String QUERY_FIND_IDENTIFIED_RISK_DTO_BY_ID =
            "SELECT NEW com.trevorgowing.projectlog.log.risk.IdentifiedRiskDTO(r.id, r.summary, r.description, " +
            "r.category, r.impact, r.status, r.dateClosed, r.project.id, r.project.code, r.project.name, r.owner.id,  " +
            "r.owner.email, r.owner.firstName, r.owner.lastName, r.probability, r.riskResponse) " +
            "FROM Risk r " +
            "WHERE r.id = ?1 ";

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(QUERY_FIND_IDENTIFIED_RISK_DTO_BY_ID)
    IdentifiedRiskDTO findIdentifiedRiskDTOById(long riskId);
}
