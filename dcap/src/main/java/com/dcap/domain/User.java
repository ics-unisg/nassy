package com.dcap.domain;

import com.dcap.filters.ENUMERATED_TYPES;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


/**
 * This class describes a user as it is stored in the database. The user is an researcher that interacts with this application.
 *
 * @author uli
 *
 */

@Entity
@Table(name = "users")
public class User implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_user")
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    @JsonIgnore
    private String password;
    private String role;



    /**
     * default constructor
     */
    public User() {
    }


    /**
     * constructor
     *
     * @param firstname firstname of the user
     * @param lastname lastname of the user
     * @param email emailadress of the user
     * @param password password of the user
     * @param role role, the user has
     */
    public User(String firstname, String lastname, String email, String password, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public void setPassword(String passwort) {
        this.password = passwort;
    }

    public String getRole() {
        return role;
    }

    public ENUMERATED_ROLES getRoleEnum() {
        return ENUMERATED_ROLES.valueOf(role);
    }

    public void setRole(String role) {

        this.role = role;
    }

    private ENUMERATED_ROLES getEnumerated_role(String role) {
        ENUMERATED_ROLES enumerated_role = null;
        try {
            enumerated_role = ENUMERATED_ROLES.valueOf(role);
        } catch (IllegalArgumentException e) {
            enumerated_role=ENUMERATED_ROLES.user;

        }
        return enumerated_role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
