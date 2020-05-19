package com.dcap.transferObjects;

import com.dcap.filters.FilterRequest;
import com.dcap.filters.IDataFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.stream.Collectors;

public class EasyFilterRequest {
    private List<Long> files;
    private List<EasyFilter> filters;
    private String decimalSeparator;

    public EasyFilterRequest(List<Long> files, List<EasyFilter> filters, String decimalSeparator) {
        this.files = files;
        this.filters = filters;
        this.decimalSeparator = decimalSeparator;
    }

    public EasyFilterRequest() {
    }

    @JsonIgnore
    public FilterRequest getRequest(){
        List<IDataFilter> fiterList = filters.stream().map(f -> f.getFullFilter()).collect(Collectors.toList());
        FilterRequest request = new FilterRequest(files, fiterList, decimalSeparator);
        return request;
    }

    @JsonIgnore
    public String getName(){
        return filters.get(0).getName();
    }

    public List<Long> getFiles() {
        return files;
    }

    public void setFiles(List<Long> files) {
        this.files = files;
    }

    public List<EasyFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<EasyFilter> filters) {
        this.filters = filters;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }
}
