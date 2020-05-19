package com.dcap.security;

import com.dcap.domain.User;
import com.dcap.domain.User;
import com.dcap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username);
        System.out.println(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        System.err.println(user.getEmail() + " "+ user.getEmail() + " " + user.getPassword() + " "+ user.getRole());
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        System.out.println(myUserPrincipal.getAuthorities().iterator().next().getAuthority());
        return myUserPrincipal;
    }
}