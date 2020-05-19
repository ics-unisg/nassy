package com.dcap.rest.admin;

import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.transferObjects.EasyUser;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.UserService;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.transferObjects.EasyUser;
import com.dcap.transferObjects.EasyUserData;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRestAdmin {

    final UserService userService;
    final MySecurityAccessorInterface mySecurityAccessor;


    @Autowired
    public UserRestAdmin(UserService userService, MySecurityAccessorInterface mySecurityAccessor) {
        this.userService = userService;
        this.mySecurityAccessor = mySecurityAccessor;
    }


    public ResponseEntity<DataMsg<EasyUser>> createNewUser(EasyUser newUser) {
        User savedUser;
        try {
            savedUser = userService.save(newUser.createUser());
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(801, null, repoExeption.getMessage(), null));
        }
        return ResponseEntity.ok().body(new DataMsg(0, null, null, new EasyUser(savedUser)));
    }

    public ResponseEntity<DataMsg<List<EasyUser>>> getAllUsers() {
        List<User> allUser = userService.getAllUser();
        List<EasyUser> easyUsersList = allUser.stream().map(u -> new EasyUser(u)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new DataMsg(0, null, null, easyUsersList));
    }

    public ResponseEntity<DataMsg<List<EasyUser>>> findUserById(Long id) {
        User userById;
        try {
            userById = userService.findUserById(id);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(802, null, repoExeption.getMessage(), null));
        }
        EasyUser easyUser = new EasyUser(userById);
        return ResponseEntity.ok().body(new DataMsg(0, null, null, easyUser));

    }

    public ResponseEntity<DataMsg<Boolean>> deleteUser(Long id) {
        try {
            userService.delete(id);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(802, null, repoExeption.getMessage(), null));
        }
        return ResponseEntity.ok().body(new DataMsg(0, null, null, true));
    }

    public ResponseEntity<DataMsg<String>> resetPassword(Long id) {
        User userById;
        try {
            userById = userService.findUserById(id);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(802, null, repoExeption.getMessage(), null));
        }
        String pwd = RandomStringUtils.randomAlphanumeric(25);;

        try {
            userService.updatePassword(userById, pwd);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(802, null, repoExeption.getMessage(), null));
        }
        return ResponseEntity.ok().body(new DataMsg(0, null, null, pwd));

    }


}
