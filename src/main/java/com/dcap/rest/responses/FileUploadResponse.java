package com.dcap.rest.responses;

import com.dcap.transferObjects.EasyUserData;
import com.dcap.transferObjects.EasyUserData;

import java.util.List;

/**
 * Class that is used as response for Uploading files
 *
 * @author uli
 */
public class FileUploadResponse {
    private String message;
    private List<EasyUserData>  easyUserDataListNotMapped;
    private List<EasyUserData> easyUserDataListSuccessfultMapped;

    /**
     * Constructor for the response, contains information about which files ares mapped to users and which not
     *
     * @param easyUserDataListNotMapped list of files unssuccessfully mapped
     * @param easyUserDataListSuccessfultMapped list of files successfully mapped
     */
    public FileUploadResponse(List<EasyUserData> easyUserDataListNotMapped, List<EasyUserData> easyUserDataListSuccessfultMapped) {
        this.easyUserDataListNotMapped = easyUserDataListNotMapped;
        this.easyUserDataListSuccessfultMapped = easyUserDataListSuccessfultMapped;
    }

    /**
     * Defauld constructor
     */
    public FileUploadResponse() {
    }

    /**
     * Constructor for Error message
     * @param message Error message
     */
    public FileUploadResponse(String message) {
        this.message= message;
    }

    /**
     * Getter for all unmapped files
     * @return list of unmapped files
     */
    public List<EasyUserData> getEasyUserDataListNotMapped() {
        return easyUserDataListNotMapped;
    }

    /**
     * Getter for all mapped files
     * @return list of mapped files
     */
    public List<EasyUserData> getEasyUserDataListSuccessfultMapped() {
        return easyUserDataListSuccessfultMapped;
    }

    /**
     * Gets error message
     * @return Error message
     */
    public String getMessage() {
        return message;
    }
}
