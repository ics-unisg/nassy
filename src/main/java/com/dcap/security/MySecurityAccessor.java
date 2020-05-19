package com.dcap.security;

import com.dcap.domain.User;
import com.dcap.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MySecurityAccessor implements MySecurityAccessorInterface {

    @Override
    public User getCurrentUser() {
        System.out.println("GetPrincipal function real");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal details = (MyUserPrincipal) authentication.getPrincipal();
        User user = details.getUser();
        return user;
    }
}
