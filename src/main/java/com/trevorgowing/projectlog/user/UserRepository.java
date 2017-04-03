package com.trevorgowing.projectlog.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

interface UserRepository extends PagingAndSortingRepository<User, Long> {

    String QUERY_FIND_USER_DTOS =
            "SELECT NEW com.trevorgowing.projectlog.user.UserDTO(u.id, u.email, u.password, u.firstName, u.lastName) " +
            "FROM User u ";

    @Query(QUERY_FIND_USER_DTOS)
    List<UserDTO> findUserDTOs();

    String QUERY_FIND_USER_DTO_BY_ID =
            "SELECT NEW com.trevorgowing.projectlog.user.UserDTO(u.id, u.email, u.password, u.firstName, u.lastName) " +
            "FROM User u " +
            "WHERE u.id = ?1 ";

    @Query(QUERY_FIND_USER_DTO_BY_ID)
    Optional<UserDTO> findUserDTOById(long userId);
}
