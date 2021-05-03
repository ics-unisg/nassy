package com.dcap.transferObjects;

import java.util.List;

/**
 * Class to calculate measures of a tsv file.
 * This is a relict of the old cheetah and not implemented in the same way a other filters.
 * should not further developed
 */
public class MeasureRequest {
    private List<Long> fileIds;
    private List<String> labels;
    private String labelColumn;
    private String baseline;
    private List<String> columnNames;
    private String timeStampColumnName;



    /**
     * Instantiates a new Measure request.
     * @param fileIds     the ids of the files that should be used for the calculation
     * @param labels      the labels labels that are attached to the differen fields
     * @param labelColumn the name of the column that keeps the lablels
     * @param baseline    the name of the baselinelabel
     * @param columnNames the names of the columns that keep the values for the calculation
     * @param timeStampColumnName
     */
    public MeasureRequest(List<Long> fileIds, List<String> labels, String labelColumn, String baseline, List<String> columnNames, String timeStampColumnName) {
        this.fileIds = fileIds;
        this.labels = labels;
        this.labelColumn = labelColumn;
        this.baseline = baseline;
        this.columnNames = columnNames;
        this.timeStampColumnName = timeStampColumnName;
    }

    /**
     * Instantiates a new Measure request.Default constructor
     */
    public MeasureRequest() {
    }

    /**
     * Gets file ids.
     *
     * @return the file ids
     */
    public List<Long> getFileIds() {
        return fileIds;
    }

    /**
     * Gets labels.
     *
     * @return the labels
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * Gets label column.
     *
     * @return the label column
     */
    public String getLabelColumn() {
        return labelColumn;
    }

    /**
     * Gets baseline.
     *
     * @return the baseline
     */
    public String getBaseline() {
        return baseline;
    }

    /**
     * Gets column names.
     *
     * @return the column names
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    public String getTimeStampColumnName() {
        return timeStampColumnName;
    }
}
