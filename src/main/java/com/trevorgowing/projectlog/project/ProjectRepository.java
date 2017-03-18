package com.trevorgowing.projectlog.project;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {
}
