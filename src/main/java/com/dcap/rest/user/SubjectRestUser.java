package com.dcap.rest.user;

import com.dcap.domain.Study;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.EasySubject;
import com.dcap.domain.Study;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.*;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.EasySubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectRestUser {
    final UserServiceInterface userServiceInterface;
    final SubjectServiceInterface subjectService;
    final StudyServiceInterface studyServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public SubjectRestUser(UserDataServiceInterface userDataServiceInterface, UserServiceInterface userServiceInterface, SubjectServiceInterface subjectService, StudyServiceInterface studyServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {
        this.userServiceInterface = userServiceInterface;
        this.subjectService = subjectService;
        this.studyServiceInterface = studyServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<List<EasySubject>>> getSubjects() {
        User user = mySecurityAccessor.getCurrentUser();
//        try {
//            user = userService.getUserById(userId);
//        } catch (RepoExeption exeption) {
//            exeption.printStackTrace();
//        }
        List<Subject> subjects = subjectService.getAllSubjectsForUser(user);
        List<EasySubject> easySubjectList = subjects.stream().map(s -> new EasySubject(s)).collect(Collectors.toList());
        return ResponseEntity.ok(new DataMsg(0, null, null, easySubjectList));
    }

    public ResponseEntity<DataMsg<List<EasySubject>>> getSubjectsForStudy(Long studyId) {
        User user = mySecurityAccessor.getCurrentUser();
        Study studyById = null;
        try {
            studyById = studyServiceInterface.findStudyById(studyId);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1501, null, "Not owner of study", null));

        }
        User userOwningStudy = studyById.getUsers();
        if (!user.getId().equals(userOwningStudy.getId())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1502, null, "Not owner of study", null));
        }
        List<Subject> subjects = subjectService.getAllSubjectsForStudy(studyById);
        List<EasySubject> easySubjectList = subjects.stream().map(s -> new EasySubject(s)).collect(Collectors.toList());
        return ResponseEntity.ok(new DataMsg(0, null, null, easySubjectList));
    }

    public ResponseEntity<DataMsg<EasySubject>> addSubject(EasySubject subject) {

        User user = mySecurityAccessor.getCurrentUser();

        Subject fullSubject = null;
        try {
            fullSubject = subject.getFullSubject(studyServiceInterface);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1502, null, exeption.getMessage(), null));

        }
        if (!user.getId().equals(fullSubject.getStudy().getUsers().getId())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1502, null, "Not owner of study", null));
        }
        if (fullSubject.getId() != null) {
            if (subjectService.sujbectExists(fullSubject.getId())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1503, null,  "Subject already exists", null));

            }

        }

        Subject savedSubject = null;
        try {
            savedSubject = subjectService.save(fullSubject, user);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1503, null, exeption.getMessage(), null));
        }
        EasySubject easySubjectSaved = new EasySubject(savedSubject);


        return ResponseEntity.ok(new DataMsg(0, null, null, easySubjectSaved));
    }

    public ResponseEntity<DataMsg> deleteSubject(Long subjectId) {
        User user = mySecurityAccessor.getCurrentUser();
        Subject subjectById = null;
        try {
            subjectById = subjectService.getSubjectById(subjectId);
        } catch (RepoExeption e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1504, null, e.getMessage(), null));
        }
        if (!subjectById.getStudy().getUsers().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataMsg(1504, null, "Not owner of study", null));
        }
        try {
            subjectService.delete(subjectId);
        } catch (RepoExeption e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataMsg(1504, null, e.getMessage(), null));
        }
        return ResponseEntity.ok(new DataMsg(0, null, "Deleted subject with id " + subjectId, null));
    }

    public ResponseEntity<DataMsg<EasySubject>> updateSubject(EasySubject subject) {
        User user = mySecurityAccessor.getCurrentUser();


        Subject fullSubject = null;
        try {
            fullSubject = subject.getFullSubject(studyServiceInterface);
        } catch (RepoExeption exeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1504, null, exeption.getMessage(), null));
        }
        if (!fullSubject.getStudy().getUsers().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1504, null, "Not owner of subject", null));
        }
        Subject savedSubject = null;
        try {
            savedSubject = subjectService.updateSubject(fullSubject);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1503, null, exeption.getMessage(), null));
        }
        EasySubject easySubjectSaved = new EasySubject(savedSubject);


        return ResponseEntity.ok(new DataMsg(0, null, null, easySubjectSaved));
    }


    public ResponseEntity<DataMsg<List<EasySubject>>> getSubject(Long subjectId) {
        User user = mySecurityAccessor.getCurrentUser();
        Subject subjectById;
        try {
            subjectById = subjectService.getSubjectById(subjectId);
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1504, null, "No such subject found.", null));
        }

        if(!subjectById.getStudy().getUsers().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1504, null,  "No such subject found.", null));
        }

        EasySubject easySubjectSaved = new EasySubject(subjectById);


        return ResponseEntity.ok(new DataMsg(0, null, null, easySubjectSaved));

    }
}
