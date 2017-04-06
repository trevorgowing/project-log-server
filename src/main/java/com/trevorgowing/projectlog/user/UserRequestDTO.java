package com.trevorgowing.projectlog.user;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
class UserRequestDTO implements Serializable {

    private static final long serialVersionUID = 1507127704121652160L;

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public UserRequestDTO(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    static UserRequestDTO completeUserRequest(String email, String password, String firstName, String lastName) {
        return new UserRequestDTO(email, password, firstName, lastName);
    }
}
