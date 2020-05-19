package com.dcap.service.serviceInterfaces;

import com.dcap.domain.User;
import com.dcap.service.Exceptions.RepoExeption;

public interface UserServiceInterface {
    User getUserById(Long id) throws RepoExeption;

    User save(User u) throws RepoExeption;

    User updatePassword(User user, String password) throws RepoExeption;

    User findUserById(Long userId) throws RepoExeption;

    void delete(User user);

    Boolean delete(Long id) throws RepoExeption;

    User findUserByEmail(String username);
}
