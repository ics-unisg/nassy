package com.dcap.rest.user;

import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.rest.DataMsg;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.transferObjects.EasyUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class TaskRestUser {


    final UserDataServiceInterface userDataServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public TaskRestUser(UserDataServiceInterface userDataServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {
        this.userDataServiceInterface = userDataServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }


    public ResponseEntity<DataMsg<EasyUserData>> getUserDataByTaskId(String taskId) {
        User user = mySecurityAccessor.getCurrentUser();
        UserData userDataByTaskId = null;
        try {
            userDataByTaskId = userDataServiceInterface.getUserDataByTaskId(taskId);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1601, null, repoExeption.getMessage(), null));

        }
        if (!userDataByTaskId.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1601, null, "Not owner of Task", null));

        }
        return ResponseEntity.ok(new DataMsg(0, null, null, new EasyUserData(userDataByTaskId)));
    }

    public ResponseEntity<DataMsg<Boolean>> checkIfTaskFinished(String taskId) {
        UserData userDataByTaskId = null;
        try {
            userDataByTaskId = userDataServiceInterface.getUserDataByTaskId(taskId);
        } catch (RepoExeption e) {
            return ResponseEntity.ok(new DataMsg(0, "not finished", null, false));
        }


        return ResponseEntity.ok(new DataMsg(0, "finished", null, true));
    }
}
