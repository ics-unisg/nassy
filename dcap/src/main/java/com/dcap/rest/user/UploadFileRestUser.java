package com.dcap.rest.user;

import com.dcap.domain.*;
import com.dcap.helper.Pair;
import com.dcap.rest.DataMsg;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.service.storage.StorageService;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.domain.*;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.service.*;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.storage.StorageService;
import com.dcap.helper.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UploadFileRestUser {

    private final UserDataServiceInterface userDataServiceInterface;
    private final SubjectService subjectService;
    private final StorageService storageService;
    private final NotificationService notificationService;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public UploadFileRestUser(UserDataServiceInterface userDataServiceInterface, SubjectService subjectService, StorageService storageService, NotificationService notificationService, MySecurityAccessorInterface mySecurityAccessor) {
        this.userDataServiceInterface = userDataServiceInterface;
        this.subjectService = subjectService;
        this.storageService = storageService;
        this.notificationService = notificationService;
        this.mySecurityAccessor = mySecurityAccessor;
    }




    /**
     * Method to upload a list of files from the client.
     * This class can be only used to upload csv and tsv files. Other files will be rejected, and videos can be uploaded using
     * the {handleVideoUpload(List)} method.
     * If the files have a certain format [subjectid]@[studyname]@name.tsv|csv, they can automatically be mapped
     * @param files list of files from
     * @return Response entity containing the list of mapped and not mapped files as {@link EasyUserData}, or an error containing a error messagek
     */
    public ResponseEntity<DataMsg<FileUploadResponse>> handleFileUpload(List<MultipartFile> files,
                                                                        List<Long> subjectIds) {
        User user = mySecurityAccessor.getCurrentUser();

        if(files.stream().anyMatch(s->(!(s.getOriginalFilename().endsWith("tsv") || s.getOriginalFilename().endsWith("csv"))))){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new DataMsg<>(1701,null, "At least one file has a wrong extension, " +
                    "you need \"tsv\" of \"csv\".", null));
        }
        if(subjectIds!=null&&!subjectIds.isEmpty()){
            try {
                return handleFileUploadViaUserIds(files, subjectIds, user);
            } catch (RepoExeption repoExeption) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1702, null, repoExeption.getMessage(), null));
            }
        }
        return handleFileUploadViaName(files, user);
    }

    private ResponseEntity<DataMsg<FileUploadResponse>> handleFileUploadViaName(List<MultipartFile> files, User user) {

        List<Subject> subjectList= new ArrayList<>();
        ArrayList<EasyUserData> easyUserDataListSuccessfultMapped = new ArrayList<>(); //could be used for display successfull mapping
        ArrayList<String> originalFilenames = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            originalFilenames.add(originalFilename);
            Subject subjectBySubjectId;
            if (originalFilename.contains("@")) {
                String[] split = originalFilename.split("@");
                if (split.length > 2) {
                    String subject = split[0];
                    String study = split[1];
                    try {
                        subjectBySubjectId = subjectService.getSubjectBySubjectAndStudy(subject, study, user);
                        subjectList.add(subjectBySubjectId);
                    }catch (RepoExeption repoExeption){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1702, null, repoExeption.getMessage(), null));
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1703, null, "Does not fullfill naming convention", null));
                }
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1703, null, "Does not fullfill naming convention", null));
            }
        }

        saveTheFiles(files, originalFilenames, user, subjectList, easyUserDataListSuccessfultMapped);

        return ResponseEntity.status(HttpStatus.OK).body(new DataMsg<>(0, null, null, new FileUploadResponse(null, easyUserDataListSuccessfultMapped)));
    }

    private void saveTheFiles(List<MultipartFile> files, List<String> originalFilenames, User user, List<Subject> subjectList, ArrayList<EasyUserData> easyUserDataListSuccessfultMapped) {
        for(int i = 0; i<subjectList.size(); i++){
            Pair<String, String> pathNamePair = storageService.store(files.get(i), "data");
            String path = pathNamePair.getKey();
            String originalFilename = originalFilenames.get(i);
//            String originalFilename = files.get(i).getOriginalFilename();
            UserData userData = new UserData(subjectList.get(i),  null, originalFilename, "depends", path, false, null, null, ENUMERATED_CATEGORIES.DATA, user, null);
            UserData userDataOutOfDataBase = userDataServiceInterface.saveOrUpdate(userData);
            EasyUserData easyUserData = new EasyUserData(userDataOutOfDataBase);
            easyUserDataListSuccessfultMapped.add(easyUserData);
            Notifications notifications = new Notifications(user, "Uploaded new file "+ originalFilename, "success", path, false, new Date(), null);
            notificationService.safeOrUpdate(notifications);
        }
    }

    private ResponseEntity<DataMsg<FileUploadResponse>> handleFileUploadViaUserIds(List<MultipartFile> files, List<Long> subjectIds, User user) throws RepoExeption {
        ArrayList<EasyUserData> easyUserDataListSuccessfultMapped = new ArrayList<>(); //could be used for display successfull mapping
        ArrayList<String> originalFilenames = new ArrayList<>();

        if(files.size() != subjectIds.size()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new DataMsg<>(1704, null, "List of Files not same size as list of subjcts", null));
        }
        List<Subject> subjectList= new ArrayList<>();
        for(Long id: subjectIds){
            Subject subjectById = subjectService.getSubjectById(id);
            subjectList.add(subjectById);
        }
        for(MultipartFile file:files){
            originalFilenames.add(file.getOriginalFilename());
        }
        saveTheFiles(files, originalFilenames, user, subjectList, easyUserDataListSuccessfultMapped);
        return ResponseEntity.status(HttpStatus.OK).body(new DataMsg<>(0, null, null,new FileUploadResponse(null, easyUserDataListSuccessfultMapped)));

    }

}
