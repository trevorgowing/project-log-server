package com.trevorgowing.projectlog.log;

import org.springframework.data.repository.PagingAndSortingRepository;

interface LogRepository extends PagingAndSortingRepository<Log, Long> {}
