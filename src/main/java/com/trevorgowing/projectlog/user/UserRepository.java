package com.trevorgowing.projectlog.user;

import org.springframework.data.repository.PagingAndSortingRepository;

interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
