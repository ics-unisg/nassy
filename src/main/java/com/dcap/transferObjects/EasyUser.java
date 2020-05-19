package com.dcap.transferObjects;

import com.dcap.domain.ENUMERATED_ROLES;
import com.dcap.domain.User;

public class EasyUser {

    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private ENUMERATED_ROLES role;

    public EasyUser(User user ) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.password = null;
        this.role = user.getRoleEnum();
    }

    public EasyUser() {
    }

    public User createUser(){

        User user = new User(firstname, lastname, email, password, role.toString());
        if(id!=null){
            user.setId(this.id);
        }
        return user;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public ENUMERATED_ROLES getRole() {
        return role;
    }

    public void setRole(ENUMERATED_ROLES role) {
        this.role = role;
    }
}
