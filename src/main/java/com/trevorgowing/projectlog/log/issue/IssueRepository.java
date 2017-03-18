package com.trevorgowing.projectlog.log.issue;

import org.springframework.data.repository.PagingAndSortingRepository;

interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {
}
