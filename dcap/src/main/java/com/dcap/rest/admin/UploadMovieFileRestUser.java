package com.dcap.rest.admin;

import com.dcap.domain.ENUMERATED_CATEGORIES;
import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.*;
import com.dcap.rest.DataMsg;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.service.storage.StorageService;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.domain.*;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.*;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.service.storage.StorageService;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.helper.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UploadMovieFileRestUser {
    private final UserDataServiceInterface userDataServiceInterface;
    private final StorageService storageService;
    private final NotificationService notificationService;
    private final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public UploadMovieFileRestUser(UserDataServiceInterface userDataServiceInterface, StorageService storageService, NotificationService notificationService, MySecurityAccessorInterface mySecurityAccessor) {
        this.userDataServiceInterface = userDataServiceInterface;
        this.storageService = storageService;
        this.notificationService = notificationService;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    /**
     * Method to upload a list of files from the client.
     * This class can be only used to upload wbm files. Other files will be rejected.
     * @param files list of files from
     * @return Response entity containing the list of mapped and not mapped files as {@link EasyUserData}, or an error containing a error messagek
     */
    public ResponseEntity<DataMsg<FileUploadResponse>> handleFileUpload(List<MultipartFile> files, List<Long> userDataIds) {
        User user = mySecurityAccessor.getCurrentUser();

        if(files.stream().anyMatch(s->(!(s.getOriginalFilename().endsWith("webm"))))){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new DataMsg<>(601, null,"Only webm files allowed", null));
        }
        if(userDataIds!=null&&!userDataIds.isEmpty()){
            try {
                return handleFileUploadViaUserDataIds(files, userDataIds, user);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(602, null, e.getMessage(), null));

            } catch (FileException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(602, null, e.getMessage(), null));

            }
        }

        try {
            return handleFileUploadViaName(files, user);
        } catch (RepoExeption repoExeption) {
            repoExeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(602, null, repoExeption.getMessage(), null));        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(602, null, e.getMessage(), null));
        } catch (FileException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(602, null, e.getMessage(), null));

        }
    }




    private ResponseEntity<DataMsg<FileUploadResponse>> handleFileUploadViaName(List<MultipartFile> files, User user) throws RepoExeption, IOException, FileException {
        List<UserData> userDataList= new ArrayList<>();
        ArrayList<EasyUserData> easyUserDataListSuccessfultMapped = new ArrayList<>(); //could be used for display successfull mapping

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.endsWith(".webm")) {
                String likeName = originalFilename.replaceAll("\\.webm$", "");
                UserData byName = userDataServiceInterface.getByName(likeName, user.getId());
                userDataList.add(byName);
            }else{
                throw new RepoExeption("Does not fullfill naming convention");
            }
        }

        try {
            saveTheFiles(files, user, easyUserDataListSuccessfultMapped, userDataList);
        } catch (DoubleColumnException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new DataMsg<>(602, null, "Double ColumnName: "+e.getMessage(), null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new DataMsg(0, null, null,(new FileUploadResponse(null, easyUserDataListSuccessfultMapped))));
    }

    private ResponseEntity<DataMsg<FileUploadResponse>> handleFileUploadViaUserDataIds(List<MultipartFile> files, List<Long> subjectIds, User user) throws IOException, FileException {
        ArrayList<EasyUserData> easyUserDataListSuccessfultMapped = new ArrayList<>(); //could be used for display successfull mapping
        if(files.size() != subjectIds.size()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new DataMsg<>(603, null, "List of Files not same size as list of userdata", null));
        }
        List<UserData> userDataList= new ArrayList<>();
        for(Long id: subjectIds){
            UserData userDataById = null;
            try {
                userDataById = userDataServiceInterface.getUserDataById(id);
            } catch (RepoExeption exeption) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new DataMsg<>(604, null, "no such file", null));
            }
            userDataList.add(userDataById);
        }
        try {
            saveTheFiles(files, user, easyUserDataListSuccessfultMapped, userDataList);
        } catch (DoubleColumnException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new DataMsg<>(602, null, "Double ColumnName: "+e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new DataMsg<>(0, null, null,new FileUploadResponse(null, easyUserDataListSuccessfultMapped)));

    }

    private void saveTheFiles(List<MultipartFile> files, User user, ArrayList<EasyUserData> easyUserDataListSuccessfultMapped, List<UserData> userDataList) throws IOException, FileException, DoubleColumnException {
        for(int i = 0; i<userDataList.size(); i++){
            Pair<String, String> pathNamePair = storageService.store(files.get(i), "data");
            String path = pathNamePair.getKey();
            String originalFilename = pathNamePair.getValue();
            Long startTimestamp = getStartTimestamp(userDataList.get(i), null, null, null);
            UserData userData = new UserData(userDataList.get(i).getSubject(), null, originalFilename, "depends", path, false, null, startTimestamp, ENUMERATED_CATEGORIES.MOVIE, user, null);
            UserData userDataOutOfDataBase = userDataServiceInterface.saveOrUpdate(userData);
            EasyUserData easyUserData = new EasyUserData(userDataOutOfDataBase);
            easyUserDataListSuccessfultMapped.add(easyUserData);
            Notifications notifications = new Notifications(user, "Uploaded new file " + originalFilename, "success", path, false, new Date(), null);
            notificationService.safeOrUpdate(notifications);

        }
    }

    private Long getStartTimestamp(UserData userData, String timestampColumn, String seperator, String decimalSeperator) throws IOException, FileException, DoubleColumnException {
        if(timestampColumn==null || timestampColumn.trim().equals("")){
            timestampColumn="EyeTrackerTimestamp";
        }
        if(decimalSeperator==null || decimalSeperator.trim().equals("")){
            decimalSeperator=",";
        }
        if(seperator==null || seperator.trim().equals("")){
            if(userData.getFilename().endsWith("tsv")){
                seperator="\t";
            }else if(userData.getFilename().endsWith("csv")){
                seperator=";";
            }else{
            seperator=";";
            }
        }

        DataFile dataFile = new DataFile(new File(userData.getPath()), userData.getPath(), seperator,true, decimalSeperator);
        DataFileColumn column = dataFile.getHeader().getColumn(timestampColumn);
        return DataFileUtils.getTimeStamps(dataFile, column, true)[0];
    }
}
