package com.dcap.rest.user;

import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.UserService;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRestUser {

    final UserServiceInterface userService;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public UserRestUser(UserService userService, MySecurityAccessorInterface mySecurityAccessor) {
        this.userService = userService;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<Boolean>> updatePasswort(String password) {
        User user = mySecurityAccessor.getCurrentUser();

        try {
            User updatedUser = userService.updatePassword(user, password);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(011, null, repoExeption.getMessage(), false));
        }
        return ResponseEntity.ok().body(new DataMsg(000, null, null, true));

    }

}
