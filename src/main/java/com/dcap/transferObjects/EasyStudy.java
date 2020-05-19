package com.dcap.transferObjects;

import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import io.swagger.annotations.ApiParam;

public class EasyStudy {
    private Long id;
    private String name;
    private String comment;
    private Long synchronized_from;
    private Long userId;

    public EasyStudy() {
    }

    public EasyStudy(Study study) {
        this.id = study.getId();
        this.name = study.getName();
        this.comment = study.getComment();
        this.synchronized_from = study.getSynchronized_from();
        this.userId = study.getUsers().getId();
    }

    public EasyStudy(Long id, String name, String comment, Long synchronized_from, Long userId) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.synchronized_from = synchronized_from;
        this.userId = userId;
    }

    public Study getFullStudy(User user){
        Study study = new Study(this.name, this.comment, this.synchronized_from, user);
        if(this.id==null){
            study.setId(this.id);
        }
        return study;
    }

    public Study getFullStudy(UserServiceInterface userServiceInterface) throws RepoExeption {
        User userById = userServiceInterface.findUserById(this.userId);
        return getFullStudy(userById);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
