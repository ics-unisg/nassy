package com.dcap.security;

import com.dcap.domain.User;
import com.dcap.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyUserPrincipal implements UserDetails {

    public static class MyAuthority implements GrantedAuthority {
        private final String role;

        public MyAuthority(String role) {
            this.role = role;
        }

        @Override
        public String getAuthority() {
            return this.role;
        }
    }

    private User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(user.getRole())
                .map(role -> new MyAuthority(role))
                .collect(Collectors.toList());
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        //return user.getFirstname() + " " + user.getLastname();
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}