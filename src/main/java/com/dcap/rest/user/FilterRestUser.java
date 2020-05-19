package com.dcap.rest.user;

import com.dcap.domain.User;
import com.dcap.filters.FilterRequest;
import com.dcap.helper.FilterException;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.PermissionException;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.service.threads.WorkerController;
import com.dcap.transferObjects.EasyFilterRequest;
import com.dcap.transferObjects.MeasureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FilterRestUser {

    final WorkerController workerController;
    final UserServiceInterface userServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public FilterRestUser(WorkerController workerController, UserServiceInterface userServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {

        this.workerController = workerController;
        this.userServiceInterface = userServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }


    public ResponseEntity<DataMsg<String>> makeFilterRequest(EasyFilterRequest filter) {
        FilterRequest request = filter.getRequest();
        String uuidOfTask = null;
        User user = mySecurityAccessor.getCurrentUser();
        try {
            uuidOfTask = workerController.addTask(request, user);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1301, null, exeption.getMessage(), null));
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1302, null, e.getMessage(), null));

        } catch (FilterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1303, null, e.getMessage(), null));
        }
        return ResponseEntity.ok(new DataMsg<>(0, null, null, uuidOfTask));


    }

    public ResponseEntity<DataMsg<String>> calculateMeasure(MeasureRequest request) {
        User user = mySecurityAccessor.getCurrentUser();
        String uuidOfTask;
        try {
            uuidOfTask = workerController.addTask(request.getFileIds(), request.getLabels(), request.getLabelColumn(), request.getBaseline(), request.getColumnNames(), request.getTimeStampColumnName(), user);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1304, null, exeption.getMessage(), null));

        }
        return ResponseEntity.ok(new DataMsg<>(0, null, null, uuidOfTask));

    }



}
