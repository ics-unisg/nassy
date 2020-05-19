package com.dcap.rest.admin;

import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.EasyStudy;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.EasyStudy;
import com.dcap.transferObjects.EasyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyRestAdmin {

    final StudyServiceInterface studyServiceInterface;
    final UserServiceInterface userServiceInterface;

    @Autowired
    public StudyRestAdmin(StudyServiceInterface studyServiceInterface, UserServiceInterface userServiceInterface) {
        this.studyServiceInterface = studyServiceInterface;
        this.userServiceInterface = userServiceInterface;
    }

    public ResponseEntity<DataMsg<List<EasyStudy>>> getStudiesOfUser(Long id) {
        User user;
        try {
            user = userServiceInterface.findUserById(id);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(501, null, "Study with name exists already", null));

        }
        List<Study> allStudiesForUser = studyServiceInterface.getAllStudiesForUser(user);
        List<EasyStudy> easyStudyList = allStudiesForUser.stream().map(study -> new EasyStudy(study)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, easyStudyList));
    }


    public ResponseEntity<DataMsg<Boolean>> deleteStudy(Long id) {


        Study studyById = null;
        try {
            studyById = studyServiceInterface.findStudyById(id);
        } catch (RepoExeption repoExeption) {
            repoExeption.printStackTrace();
        }
        try {
            studyServiceInterface.deleteStudy(studyById);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(502, null, repoExeption.getMessage(), null));
        }


        return ResponseEntity.ok().body(new DataMsg<>(0, null, null, true));

    }
}
