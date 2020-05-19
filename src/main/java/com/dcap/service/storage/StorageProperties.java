package com.dcap.service.storage;

import org.springframework.stereotype.Service;

/**
 * Class from spring.io tutorial
 */
@Service
public class StorageProperties {

    /**
     * Folder locationData for storing files
     */
//    private String locationData = "/home/uli/Desktop/TargetOrdner/data/";
    private String locationData = "./data/";

    private String locationPythonCode="./datapythoncode";

    public String getLocationForData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public String getLocationForPythonCode() {
        return locationPythonCode;
    }

    public void setLocationForPythonCode(String locationPythonCode) {
        this.locationPythonCode = locationPythonCode;
    }

}
