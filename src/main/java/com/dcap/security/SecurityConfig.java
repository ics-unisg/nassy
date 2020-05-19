package com.dcap.security;

import com.dcap.domain.User;
import com.dcap.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@Configuration
@EnableWebSecurity //TODO Ist das sinvoll
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;


    // Authentication : User --> Roles
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    // Authorization : Role -> Access
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasAnyAuthority("administrator")
                .antMatchers("/api/user/**").hasAnyAuthority("administrator","user")
                .antMatchers("/v2/api-docs", "/swagger*/**").permitAll().anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .formLogin();


    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }

    @Autowired
    private PasswordEncoder pwe;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(pwe);
        return authProvider;
    }




    @Bean
    public UserDetailsService userDetailsService(){
        //GrantedAuthority authority = new SimpleGrantedAuthority("administrator");
        UserDetails userDetails =  new  MyUserPrincipal(new User("uli", "lob", "u@l","test", "administrator"));
        return new InMemoryUserDetailsManager(Arrays.asList(userDetails));
    }

}
