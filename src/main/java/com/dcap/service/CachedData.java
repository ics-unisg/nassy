package com.dcap.service;

import com.dcap.transferObjects.GraphData;
import com.dcap.transferObjects.GraphData;

import java.util.List;
import java.util.Objects;

/**
 * The type Cached data. Stored data in a chache
 *
 * @author uli
 */
public class CachedData {

    /**
     * id of the file according the database
     */
    private Long id;

    /**
     * name of the column as in the file
     */
    private String columnName;

    /**
     * name of the timeStampColumn as in the file
     */
    private String timeStampColumn;

    /**
     * Data of the file in list format
     */
    private List<GraphData> data;

    /**
     * Id of the user who owns the data
     */
    private Long userId;

    /**
     * Constructor
     * @param id id of the file according the database
     * @param columnName name of the column as in the file
     * @param timeStampColumn name of the timeStampColumn as in the file
     * @param data Data of the file in list format
     * @param userId the id of the user who owns the data
     */
    public CachedData(Long id, String columnName, String timeStampColumn, List<GraphData> data, Long userId) {
        this.id = id;
        this.columnName = columnName;
        this.timeStampColumn = timeStampColumn;
        this.data = data;
        this.userId = userId;
    }

    /**
     * Function to check if this Object is the one searched
     *@param id id of the file according the database
     *@param columnName name of the column as in the file
     * @param timeStampColumn name of the timeStampColumn as in the file
     * @return <tt>true</tt> if it is the object searceh, false otherwise
     */
    public boolean doesMatch(Long id, String columnName, String timeStampColumn){
        if(this.id.equals(id) && this.columnName.equals(columnName) && this.timeStampColumn.equals(timeStampColumn)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets name of the column as in the file
     *
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets  name of the column as in the file
     *
     * @param columnName the column name
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Gets name of the timeStampColumn as in the file
     *
     * @return the time stamp column
     */
    public String getTimeStampColumn() {
        return timeStampColumn;
    }

    /**
     * Sets name of the timeStampColumn as in the file
     *
     * @param timeStampColumn the time stamp column
     */
    public void setTimeStampColumn(String timeStampColumn) {
        this.timeStampColumn = timeStampColumn;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<GraphData> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<GraphData> data) {
        this.data = data;
    }

    /**
     * Returns id of owner
     * @return userId of owner
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets id of user
     * @param userId of user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CachedData that = (CachedData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(columnName, that.columnName) &&
                Objects.equals(timeStampColumn, that.timeStampColumn) &&
                Objects.equals(data, that.data) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, columnName, timeStampColumn, userId);
    }
}
