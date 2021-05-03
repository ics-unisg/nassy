package com.dcap.transferObjects;

/**
 * Class that provides a timestamp and the value of a column
 */
public class GraphData {
    /**
     * The timestamp for the value
     */
    Long timestamp;
    /**
     * The value of the column for the timestamp
     */
    String value;

    /**
     * Instantiates a new Graph data.
     *
     * @param timestamp the timestamp
     * @param value     the value
     */
    public GraphData(Long timestamp, String value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
