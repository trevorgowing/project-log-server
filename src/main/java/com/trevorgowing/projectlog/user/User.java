package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = 2138197451190291396L;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    @SuppressWarnings("unused")
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
