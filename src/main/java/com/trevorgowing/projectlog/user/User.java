package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends AbstractAuditable<User, Long> {

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Basic(optional = true)
    @Column(nullable = true)
    private String firstName;

    @Basic(optional = true)
    @Column(nullable = true)
    private String lastName;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
