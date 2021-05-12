package com.dcap.domain;

import javax.persistence.*;

/**
 *
 * This class describes the data processing steps of some data processings (see DataProcessing.java)
 *
 * @author uli
 */

@Entity
@Table(name = "data_processing_steps")
public class DataProcessingStep implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_data_processing_step")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_data_processing")
    private DataProcessing dataProcessing;

    private String type;
    private Integer version;
    private String name;
    @Column(columnDefinition = "json")
    private String configuration;


    /**
     * Default constructor
     */
    public DataProcessingStep() {
    }

    /**
     *
     * @param dataProcessing Data processing related to this steps
     * @param type Type of data processing
     * @param version version
     * @param name name of the data processing step
     * @param configuration description of what have be done in the step, as json
     */
    public DataProcessingStep(DataProcessing dataProcessing, String type, Integer version, String name, String configuration) {
        this.dataProcessing = dataProcessing;
        this.type = type;
        this.version = version;
        this.name = name;
        this.configuration = configuration;
    }

    /**
     * The id as in the database
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }

    public void setDataProcessing(DataProcessing dataProcessing) {
        this.dataProcessing = dataProcessing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataProcessingStep that = (DataProcessingStep) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
