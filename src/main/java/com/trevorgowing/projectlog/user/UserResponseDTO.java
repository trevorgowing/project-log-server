package com.trevorgowing.projectlog.user;

import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
class UserResponseDTO extends UserRequestDTO {

    private static final long serialVersionUID = -84805295469064090L;

    private long id;

    public UserResponseDTO(long id, String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
        this.id = id;
    }

    static UserResponseDTO completeUserResponseDTO(long id, String email, String password, String firstName,
                                                          String lastName) {
        return new UserResponseDTO(id, email, password, firstName, lastName);
    }
}
