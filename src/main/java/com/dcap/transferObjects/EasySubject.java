package com.dcap.transferObjects;

import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.domain.Study;
import com.dcap.domain.Subject;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;

/**
 * This class is used for the communication via web. This class is lighter than the class this is derived of
 * This class can easily be serialized to JSON
 *
 * @author uli
 */

public class EasySubject {

    private Long id;
    private Long studyId;
    private String studyName;
    private String email;
    private String subject_id;
    private String comment;
    private Long synchronized_from;

    /**
     * default constructor
     */
    public EasySubject() {
    }

    /**
     * Constructor that build the EasySubject object out of a Subject
     *
     * @param subject that is the source for the light subject
     *
     */
    public EasySubject(Subject subject) {
        this.id = subject.getId();
        this.studyId = subject.getStudy().getId();
        this.studyName=subject.getStudy().getName();
        this.email = subject.getEmail();
        this.subject_id = subject.getSubjectId();
        this.comment = subject.getComment();
        this.synchronized_from =subject.getSynchronized_from();
    }

    public Subject getFullSubject(StudyServiceInterface studyServiceInterface) throws RepoExeption {
        Study study = studyServiceInterface.findStudyById(this.studyId);

        Subject subject = new Subject(study, this.email, this.subject_id, this.comment, this.synchronized_from);
        if(this.id!=null){
            subject.setId(id);
        }

        return subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getSynchronized_from() {
        return synchronized_from;
    }

    public void setSynchronized_from(Long synchronized_from) {
        this.synchronized_from = synchronized_from;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }
}
