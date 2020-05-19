package com.dcap.transferObjects;

import java.util.List;

public class DataProcessingRequest {

    private Long dataprocessingId;
    private List<Long> files;

    DataProcessingRequest() {
    }

    public Long getDataprocessingId() {
        return dataprocessingId;
    }

    public void setDataprocessingId(Long dataprocessingId) {
        this.dataprocessingId = dataprocessingId;
    }

    public List<Long> getFiles() {
        return files;
    }

    public void setFiles(List<Long> files) {
        this.files = files;
    }
}
