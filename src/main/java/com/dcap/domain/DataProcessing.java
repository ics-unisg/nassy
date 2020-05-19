package com.dcap.domain;

import javax.persistence.*;

/**
 *This class describes the dataprocess entity in the database
 *
 * @author uli
 */

@Entity
@Table(name = "data_processings")
public class DataProcessing implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_data_processing")
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_study")
    private Study study;

    private String name;
    private String comment;

    @Column(name = "trial_computation_configuration", columnDefinition = "json")
    private String trialComputationConfiguration;

    public DataProcessing() {
    }


    /**
     * Constructor with all parameters
     *
     * @param study Study the data processing are related to
     * @param name name of the data processing
     * @param comment comments describing the data processing
     * @param trialComputationConfiguration configuration stored a JSON
     */
    public DataProcessing(Study study, String name, String comment, String trialComputationConfiguration) {
        this.study = study;
        this.name = name;
        this.comment = comment;
        this.trialComputationConfiguration = trialComputationConfiguration;
    }

    /**
     * The id as in the database
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
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



    public String getTrialComputationConfiguration() {
        return trialComputationConfiguration;
    }

    public void setTrialComputationConfiguration(String trialComputationConfiguration) {
        this.trialComputationConfiguration = trialComputationConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataProcessing that = (DataProcessing) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
