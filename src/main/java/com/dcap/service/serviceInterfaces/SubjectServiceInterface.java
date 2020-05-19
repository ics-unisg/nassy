package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Study;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.service.Exceptions.RepoExeption;

import java.util.List;

public interface SubjectServiceInterface {
    List<Subject> getAllSubjectsForStudy(Study study);

    Subject save(Subject subject, User user) throws RepoExeption;

    Subject getSubjectById(Long id) throws RepoExeption;

    Subject getSubjectBySubjectAndStudy(String subjetId, String study, User user) throws RepoExeption;

    void delete(Subject subject);

    Subject updateSubject(Subject fullSubject) throws RepoExeption;

    List<Subject> getAllSubjectsForUser(User user);

    void delete(Long subjectId) throws RepoExeption;

    Boolean sujbectExists(Long subjectId);

    Subject addSubjectToStudy(Long subjectId, Long studyId) throws RepoExeption;

    Subject removeSubjectFromStudy(Long subjectId, Long studyId) throws RepoExeption;
}
