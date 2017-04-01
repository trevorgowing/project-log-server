package com.trevorgowing.projectlog.user;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
class UserDTO implements Serializable {

    private static final long serialVersionUID = -84805295469064090L;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
