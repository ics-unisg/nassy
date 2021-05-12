package com.dcap.transferObjects;

import com.dcap.domain.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used for the communication via web. This class is lighter than the class this is derived of
 * This class can easily be serialized to JSON
 *
 * @author uli
 */

public class EasyUserData {

    private Long id;
    private Long subjectId;
    private Long derived;
    private String type;
    @JsonIgnore
    private String path;
    private String name;
    @JsonIgnore
    private boolean hidden;
    private String comment;
    private Long startTimestamp;
    private Long userId;
    private ENUMERATED_CATEGORIES categories;


    /**
     * Constructor that build the EasyUserData object out of a UserData
     *
     * @param userData that is the source for the light userData
     *
     */
    public EasyUserData(UserData userData) {
        this.id = userData.getId();
        if(userData.getSubject()!=null){
            this.subjectId = userData.getSubject().getId();
        }else{
            this.subjectId=null;
        }
        if(userData.getDerived()!=null) {
            this.derived = userData.getDerived().getId();
        }else{
            this.derived=null;
        }
        this.type = userData.getType();
        this.path = userData.getPath();
        this.hidden = userData.isHidden();
        this.comment = userData.getComment();
        this.name=userData.getFilename();
        this.categories=userData.getCategories();
        this.startTimestamp=userData.getStartTimestamp();
        this.userId=userData.getUser().getId();
    }

    /**
     * default constructor
     */
    public EasyUserData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getDerived() {
        return derived;
    }

    public void setDerived(Long derived) {
        this.derived = derived;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public ENUMERATED_CATEGORIES getCategories() {
        return categories;
    }

    public void setCategories(ENUMERATED_CATEGORIES categories) {
        this.categories = categories;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
