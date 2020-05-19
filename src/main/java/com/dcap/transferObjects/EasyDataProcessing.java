package com.dcap.transferObjects;

import com.dcap.domain.DataProcessing;

import javax.persistence.Column;

public class EasyDataProcessing {

    private Long id;
    private Long studyId;
    private String name;
    private String comment;
    private String trialComputationConfiguration;


    public EasyDataProcessing(DataProcessing dataProcessingsById) {
        this.id = dataProcessingsById.getId();
        this.studyId = dataProcessingsById.getStudy().getId();
        this.name = dataProcessingsById.getName();
        this.comment = dataProcessingsById.getComment();
        this.trialComputationConfiguration = dataProcessingsById.getTrialComputationConfiguration();
    }

    public EasyDataProcessing() {
    }

    public EasyDataProcessing(Long id, Long studyId, String name, String comment, String trialComputationConfiguration) {
        this.id = id;
        this.studyId = studyId;
        this.name = name;
        this.comment = comment;
        this.trialComputationConfiguration = trialComputationConfiguration;
    }

    public Long getId() {
        return id;
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


    public String getTrialComputationConfiguration() {
        return trialComputationConfiguration;
    }

    @Override
    public String toString() {
        return "EasyDataProcessing{" +
                "id=" + id +
                ", studyId=" + studyId +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", trialComputationConfiguration='" + trialComputationConfiguration + '\'' +
                '}';
    }
}
