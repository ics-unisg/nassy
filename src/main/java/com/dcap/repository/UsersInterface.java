package com.dcap.repository;

import com.dcap.domain.User;
import com.dcap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UsersInterface extends JpaRepository<User, Long> {


    User findUserByEmail(String name);
    User findById(Long id);
}
