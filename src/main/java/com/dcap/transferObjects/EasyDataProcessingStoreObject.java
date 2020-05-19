package com.dcap.transferObjects;

import com.dcap.service.Exceptions.RepoExeption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class EasyDataProcessingStoreObject {

    private Long studyId;
    private String name;
    private String comment;
    private EasyFilterRequest easyFilterRequest;


    public EasyDataProcessingStoreObject() {
    }

    public EasyDataProcessingStoreObject(Long studyId, String name, String comment, EasyFilterRequest easyFilterRequest) {
        this.studyId = studyId;
        this.name = name;
        this.comment = comment;
        this.easyFilterRequest = easyFilterRequest;
    }

    @JsonIgnore
    public EasyDataProcessing getEasyDataProcessing() throws RepoExeption, JsonProcessingException {

        EasyFilterRequest easyFilterRequestWithoutFiles = new EasyFilterRequest(new ArrayList<>(), this.easyFilterRequest.getFilters(), this.easyFilterRequest.getDecimalSeparator());
        ObjectMapper objectMapper = new ObjectMapper();
        String filterString = objectMapper.writeValueAsString(easyFilterRequestWithoutFiles);
        return new EasyDataProcessing(null, studyId, name, comment, filterString);
    }

    public Long getStudyId() {
        return studyId;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public EasyFilterRequest getEasyFilterRequest() {
        return easyFilterRequest;
    }

}
