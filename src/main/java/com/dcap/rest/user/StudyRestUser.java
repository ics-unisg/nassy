package com.dcap.rest.user;

import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.transferObjects.EasyStudy;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessor;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.transferObjects.EasyStudy;
import com.sun.org.apache.bcel.internal.generic.DMUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyRestUser {
    final StudyServiceInterface studyService;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public StudyRestUser(StudyServiceInterface studyService, MySecurityAccessorInterface mySecurityAccessor) {
        this.studyService = studyService;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<EasyStudy>> saveStudy(EasyStudy easyStudy) {
        User currentUser = mySecurityAccessor.getCurrentUser();

        if (studyService.checkIfStudyForUserAndNameExists(currentUser, easyStudy.getName())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1401, null, "Study with name exists already", null));
        }
        Study fullStudy = easyStudy.getFullStudy(currentUser);
        Study savedStudy = null;
        try {
            savedStudy = studyService.save(fullStudy);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1401, null, repoExeption.getMessage(), null));
        }
        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, new EasyStudy(savedStudy)));
    }

    public ResponseEntity<DataMsg<EasyStudy>> changeStudy(EasyStudy easyStudy) {
        User currentUser = mySecurityAccessor.getCurrentUser();
        if (studyService.checkIfStudyForUserAndNameExists(currentUser, easyStudy.getName())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1401, null, "No such study with given name.", null));
        }
        Study study;
        try {
            study = studyService.findStudyById(easyStudy.getId());
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1402, null, repoExeption.getMessage(), null));
        }
        study.setComment(easyStudy.getComment());
        study.setName(easyStudy.getName());
        Study savedStudy;
        try {
            savedStudy = studyService.updateSave(study);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1401, null, repoExeption.getMessage(), null));
        }
        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, new EasyStudy(savedStudy)));
    }

    public ResponseEntity<DataMsg<List<EasyStudy>>> getAllStudiesForUser(){
        User currentUser = mySecurityAccessor.getCurrentUser();
        List<Study> allStudiesForUser = studyService.getAllStudiesForUser(currentUser);
        List<EasyStudy> easyStudyList = allStudiesForUser.stream().map(study -> new EasyStudy(study)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, easyStudyList));
    }

    //VERY DANGEROUS: YOU ARE LOOSING STUDY AND SUBJECTS
    public ResponseEntity<DataMsg<Boolean>> deleteStudy(EasyStudy easyStudy) {
        User currentUser = mySecurityAccessor.getCurrentUser();
        try {
            studyService.deleteStudy(easyStudy.getFullStudy(currentUser));
        } catch (RepoExeption repoExeption) {
            //Cant be the case
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1338, null, repoExeption.getMessage(), null));
            System.out.println(repoExeption.getMessage());
        }
        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, true));

    }

    public ResponseEntity<DataMsg<EasyStudy>> getStudy(Long id) {
        User currentUser = mySecurityAccessor.getCurrentUser();

        Study studyById;
        try {
            studyById = studyService.findStudyById(id);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1403, null, "No study with given id for this user.", null));
        }
        if(!studyById.getUsers().getId().equals(currentUser.getId())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1403, null, "No study with given id for this user.", null));

        }
        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, new EasyStudy(studyById)));
    }

    public ResponseEntity<DataMsg<Boolean>> deleteStudy(Long id) {
        User currentUser = mySecurityAccessor.getCurrentUser();

        Study studyById = null;
        try {
            studyById = studyService.findStudyById(id);
        } catch (RepoExeption repoExeption) {
            repoExeption.printStackTrace();
        }
        if(!studyById.getUsers().getId().equals(currentUser.getId())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1401, null, "No study with given id for this user.", null));

        }
        try {
            studyService.deleteStudy(studyById);
        } catch (RepoExeption repoExeption) {
            //Error not possible
            System.out.println(repoExeption.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1338, null, repoExeption.getMessage(), null));
        }

        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, true));

    }
}
