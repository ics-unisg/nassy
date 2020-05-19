package com.dcap.filters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Time slot for measures.
 *
 * @author uli
 */
public class TimeSlotForMeasures {

    private String columnName;
    private  Map<String, List<Double>> timeSlots;
    private  Map<String, Double> results;
    private Double baseline;

    /**
     * Instantiates a new TimeSlotForMeasures.
     *
     * @param columnName the column name
     * @param timeSlots  the time slots
     */
    public TimeSlotForMeasures(String columnName, Map<String, List<Double>> timeSlots) {
        this.columnName = columnName;
        this.timeSlots = timeSlots;
        this.results= new HashMap<>();
    }

    /**
     * Gets colum name.
     *
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets column name.
     *
     * @param columnName the column name
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Gets time slots. Key is the name of the label, the key is the list of all timeslots that are connected to that label.
     *
     * @return the time slots
     */
    public Map<String, List<Double>> getTimeSlots() {
        return timeSlots;
    }


    /**
     * Gets results.
     *
     * @return the results
     */
    public Map<String, Double> getResults() {
        return results;
    }

    /**
     * Gets baselineValue.
     *
     * @return the baseline
     */
    public Double getBaseline() {
        return baseline;
    }

    /**
     * Sets baselineValue
     *
     * @param baseline the baseline
     */
    public void setBaseline(Double baseline) {
        this.baseline = baseline;
    }


}
