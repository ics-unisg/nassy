package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.service.Exceptions.RepoExeption;

import javax.transaction.Transactional;
import java.util.List;

public interface StudyServiceInterface {
    List<Study> findStudyByName(String name);

    Study save(Study study) throws RepoExeption;

    void deleteStudy(Study studyToDelete) throws RepoExeption;

    Study findStudyById(Long id) throws RepoExeption;

    void delete(Long id) throws RepoExeption;

    List<Study> getAllStudiesForUser(User user);
    
    Study findStudyForUserAndName(User user, String name) throws RepoExeption;

    boolean checkIfStudyForUserAndNameExists(User currentUser, String name);

    List<Study> getListOfStudiesForUser(User user);

    Study updateSave(Study study) throws RepoExeption;
}
