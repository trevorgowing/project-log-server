package com.trevorgowing.projectlog.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

interface UserRepository extends PagingAndSortingRepository<User, Long> {

    String QUERY_FIND_USER_DTOS =
            "SELECT NEW com.trevorgowing.projectlog.user.UserDTO(u.email, u.password, u.firstName, u.lastName) " +
                    "FROM User u ";

    @Query(QUERY_FIND_USER_DTOS)
    List<UserDTO> findUserDTOs();
}
