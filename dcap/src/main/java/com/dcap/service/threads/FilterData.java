package com.dcap.service.threads;

import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.helper.FileException;
import java.io.IOException;
import java.util.List;


/**
 * Class used to transfer data from and to filters
 *
 * @author uli
 */
public class FilterData {
    private DataFile dataFile;
    private String message;
    private String name;
    private UserData userData;

    public FilterData(DataFile dataFile, String name,UserData userData, String message) {
        this.dataFile = dataFile;
        this.name = name;
        this.message = message;
        this.userData=userData;
    }
    
    public FilterData(DataFile dataFile, String name, UserData userData) {
        this(dataFile, name, userData, "");
    }



    public String appendToMessage(String toAppend){
        if(message.trim().equals("") && (!toAppend.trim().equals("")) && (!message.trim().endsWith(":"))){
            message=message + "; ";
        }
        message=message+toAppend;
        return message;
    }

    public DataFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserData getUserData() {
        return userData;
    }

    public void downSizeFile(List columns) throws IOException, FileException {
//        DataFile downsizedDataFile = this.dataFile.getDownsizedDataFile(columns);
//        this.dataFile=downsizedDataFile;
    }

    public void merge() throws IOException, FileException {
//        this.dataFile.mergeIn();
    }
}
