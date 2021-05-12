package com.dcap.rest.admin;

import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.UserService;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.transferObjects.EasyUserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDataRestAdmin {


    final UserDataServiceInterface userDataServiceInterface;
    final UserService userService;
    final MySecurityAccessorInterface mySecurityAccessor;


    @Autowired
    public UserDataRestAdmin(UserDataServiceInterface userDataServiceInterface, UserService userService, MySecurityAccessorInterface mySecurityAccessor) {
        this.userDataServiceInterface = userDataServiceInterface;
        this.userService = userService;
        this.mySecurityAccessor = mySecurityAccessor;
    }


    public ResponseEntity<DataMsg<EasyUserData>> getUserData(Long userId) {
        User userById = null;
        try {
            userById = userService.getUserById(userId);
        } catch (RepoExeption repoExeption) {
            repoExeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(701, null, repoExeption.getMessage(), null));
        }
        List<UserData> all = userDataServiceInterface.getUserDataByUser(userById);

        List<EasyUserData> collect = all.stream().map(u -> new EasyUserData(u)).collect(Collectors.toList());
        return ResponseEntity.ok(new DataMsg(0, null, null, collect));
    }

}
