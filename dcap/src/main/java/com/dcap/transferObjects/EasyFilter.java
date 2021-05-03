package com.dcap.transferObjects;


import com.dcap.filters.IDataFilter;
import com.dcap.helper.MyLittleFactory;
import com.dcap.service.Exceptions.AbstractFilterException;
import com.dcap.service.Exceptions.NoSuchFilterException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * This class is used to get the filters to be displayed in the WebClient
 *
 * @author uli
 */
public class EasyFilter {

    private String name;
    private Map<String, String> columns;
    //private Map<String, ENUMERATED_TYPES> requiredParameters;
    private Map<String, String> actualParameters;


    /**
     * default constructor
     */
    public EasyFilter(){
    }

    public EasyFilter(String name, Map<String, String> columns, Map<String, String> actualParameters) {
        this.name = name;
        this.columns = columns;
        this.actualParameters = actualParameters;
    }

    /**
     * Constructor, that transforms an IDataFilter object to an EasyFilter-Object
     *
     * @param filter
     */
    public EasyFilter(IDataFilter filter) {
        this.name = filter.getName();
        this.columns=filter.getColumns();
 //       this.requiredParameters=filter.getRequiredParameters();
    }

    /**
     * function to get the name of a filter
     * @return name of filter
     */
    public String getName() {
        return name;
    }

    /**
     * function to get the list of columns
     * @return list of all columnNames
     */
    public Map<String, String> getColumns() {
        return columns;
    }

    /**
     * function to get all parameters as name and type
     * @return all parameters as tuple
     */
//    public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
//        return requiredParameters;
//    }

    /**
     * function to return the timeStampColumn
     * @return name of the timeStampColumn
     */
//    public String getTimeStampColumn() {
//        return timeStampColumn;
//    }

//    /**
//     * returns the map with all parameters
//     * @return map of parameters, key as defined in the Filter, value as value that has to be set.
//     */
//    public Map<String, String> getActualParameters() {
//        return actualParameters;
//    }
//
//    //public IDataFilter getFullFilter(){
//     //   return MyLittleFactory.getFullFilter(name, id, columns, actualParameters);
//    //}
//

    @JsonIgnore
    public IDataFilter getFullFilter(){
        IDataFilter filter = null;
        try {
            filter = MyLittleFactory.getFilter(name, columns,actualParameters );
        } catch (NoSuchFilterException e) {
            e.printStackTrace();
            //TODO find a better handling
        } catch (AbstractFilterException e) {
            e.printStackTrace();
            //TODO find a better handling
        }
        return  filter;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public Map<String, String> getActualParameters() {
        return actualParameters;
    }

    public void setActualParameters(Map<String, String> actualParameters) {
        this.actualParameters = actualParameters;
    }


}

