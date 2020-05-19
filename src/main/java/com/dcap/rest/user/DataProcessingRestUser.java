package com.dcap.rest.user;


import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.helper.FilterException;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.service.threads.WorkerController;
import com.dcap.transferObjects.DataProcessingRequest;
import com.dcap.transferObjects.EasyDataProcessing;
import com.dcap.transferObjects.EasyDataProcessingStoreObject;
import com.dcap.transferObjects.EasyFilterRequest;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.DataProcessingService;
import com.dcap.service.Exceptions.PermissionException;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.Exceptions.UncompleteObjectException;
import com.dcap.service.serviceInterfaces.DataProcessingServiceInterface;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.service.threads.WorkerController;
import com.dcap.transferObjects.DataProcessingRequest;
import com.dcap.transferObjects.EasyDataProcessing;
import com.dcap.transferObjects.EasyDataProcessingStoreObject;
import com.dcap.transferObjects.EasyFilterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class DataProcessingRestUser {

    final DataProcessingServiceInterface dataProcessingService;
    final StudyServiceInterface studyServiceInterface;
    final WorkerController workerController;
    final UserServiceInterface userServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;


    @Autowired
    public DataProcessingRestUser(DataProcessingService dataProcessingService, StudyServiceInterface studyServiceInterface, WorkerController workerController, UserServiceInterface userServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {
        this.dataProcessingService = dataProcessingService;
        this.studyServiceInterface = studyServiceInterface;
        this.workerController = workerController;
        this.userServiceInterface = userServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<EasyDataProcessing>> getDataProcessingById(Long id) {
        User user = mySecurityAccessor.getCurrentUser();
        DataProcessing dataProcessingsById = dataProcessingService.getDataProcessingsById(id);
        if(dataProcessingsById!=null && !dataProcessingsById.getStudy().getUsers().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataMsg(1, null,
                    "No dataprocessing found." , null));
        }
        EasyDataProcessing easyDataProcessing = new EasyDataProcessing(dataProcessingsById);
        if (dataProcessingsById != null) {
            return ResponseEntity.ok(new DataMsg(0, null, null, easyDataProcessing));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(901, null, "Dataprocessing not found", null));

        }
    }

    public ResponseEntity<DataMsg<EasyDataProcessing>> saveDataProcessing(EasyDataProcessingStoreObject easyDataProcessingStoreObject) {
        User user = mySecurityAccessor.getCurrentUser();

        DataProcessing dataProcessing = null;

        try {
            dataProcessing = getDataProcessing(easyDataProcessingStoreObject, user);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(902, null, repoExeption.getMessage(), null));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(903, null, "Error in Json", null));
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataMsg(901, null, e.getMessage(), null));
        } catch (UncompleteObjectException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(902, null, e.getMessage(), null));
        }
        DataProcessing dataProcessingToReturn = dataProcessingService.saveOrUpdate(dataProcessing);
        EasyDataProcessing easyDataProcessing = new EasyDataProcessing(dataProcessingToReturn);
        return ResponseEntity.ok(new DataMsg(0, null, null, easyDataProcessing));
    }


    public DataProcessing getDataProcessing(EasyDataProcessingStoreObject easyDataProcessingStoreObject, User user) throws RepoExeption, JsonProcessingException, PermissionException, UncompleteObjectException {
        Study studyById = studyServiceInterface.findStudyById(easyDataProcessingStoreObject.getStudyId());

        if(studyById==null){
            throw new UncompleteObjectException("There is no valid study defined");
        }
        if(!studyById.getUsers().getId().equals(user.getId())){
            throw new PermissionException("User does not own study");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return new DataProcessing(studyById, easyDataProcessingStoreObject.getName(), easyDataProcessingStoreObject.getComment(), easyDataProcessingStoreObject.getEasyDataProcessing().getTrialComputationConfiguration());
    }

    public ResponseEntity<DataMsg<String>> useDataProcessing(DataProcessingRequest dataProcessingRequest) {

        User user = mySecurityAccessor.getCurrentUser();

        DataProcessing dataProcessingsById = dataProcessingService.getDataProcessingsById(dataProcessingRequest.getDataprocessingId());
        if(!dataProcessingsById.getStudy().getUsers().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(901, null, "Permission denied", null));
        }
        String trialComputationConfiguration = dataProcessingsById.getTrialComputationConfiguration();
        ObjectMapper objectMapper = new ObjectMapper();
        EasyFilterRequest filterRequest = null;
        try {
            filterRequest = objectMapper.readValue(trialComputationConfiguration, EasyFilterRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(903, null, "Error in Json", null));

        }
        filterRequest.setFiles(dataProcessingRequest.getFiles());
        System.err.println(filterRequest.getFiles());
        String uuidOfTask = null;
        try {
            uuidOfTask = workerController.addTask(filterRequest.getRequest(), user);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(904, null, "User not found", null));
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(901, null, e.getMessage(), null));
        } catch (FilterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(104, null, e.getMessage(), null));
        }
        return ResponseEntity.ok(new DataMsg<>(0, null, null, uuidOfTask));
    }


}
