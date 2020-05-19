package com.dcap.rest.user;

import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.GraphData;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.DataServiceInterface;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.GraphData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class DataRestUser {


    final UserDataServiceInterface userDataServiceInterface;
    final DataServiceInterface dataServiceInterface;
    final UserServiceInterface userServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public DataRestUser(UserDataServiceInterface userDataServiceInterface, DataServiceInterface dataServiceInterface, UserServiceInterface userServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {
        this.userDataServiceInterface = userDataServiceInterface;
        this.dataServiceInterface = dataServiceInterface;
        this.userServiceInterface = userServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<ArrayList<GraphData>>> getDataForTime(Long id, String timestamp, String name, Long start, Long stop) {

        return getDataOfFile(id, timestamp, name, start, stop);
    }

    public ResponseEntity<DataMsg<ArrayList<GraphData>>> getDataForTimeCut(Long id, String timestamp, String name) {

        return getDataOfFile(id, timestamp, name, null, null);
    }

    private ResponseEntity<DataMsg<ArrayList<GraphData>>> getDataOfFile(Long id, String timestamp, String name, Long start, Long stop) {
        User user = mySecurityAccessor.getCurrentUser();
        Long userId = user.getId();
        Long idOfUserData = null;
        try {
            idOfUserData = userDataServiceInterface.getUserDataById(id).getUser().getId();
        } catch (RepoExeption exeption) {
            exeption.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1001, null,"No data with given id", null));
        }
        if(!idOfUserData.equals(userId)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1002, null,"No data with given id", null));

        }
        if(start!=null && stop!= null && start>=stop){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1003, null,"Start timestamp is greater or equal than stop timestamp", null));

        }

        List<GraphData> graphDataArray = dataServiceInterface.isInList(id, name, timestamp, userId);
        if (graphDataArray == null) {


            DataFile dataFile = getDataFile(id);
            DataFileHeader header=null;
            graphDataArray = new ArrayList<GraphData>();
            if(dataFile ==null){
                UserData userDataById = null;
                try {
                    userDataById = userDataServiceInterface.getUserDataById(id);
                } catch (RepoExeption exeption) {
                    exeption.printStackTrace();
                }
                String path = userDataById.getPath();
                File file = new File(path);

                try {
                    String separator=null;
                    String decimalSeparator=null;
                    final long timeStart = System.nanoTime();
                    if(file.getPath().endsWith("tsv")){
                        separator= "\t";
                        decimalSeparator = ",";
                    }else
                    {
                        separator = ",";
                        decimalSeparator = ".";
                    }
                    dataFile = new DataFile(file, file.getPath(), separator, true, decimalSeparator);
                   // final long timeEnd = System.nanoTime();
                    //System.out.println("Verlaufszeit für File: " + (timeEnd - timeStart)/(1000000000) + " Sek.");
                    //final long timeStart0 = System.nanoTime();
                    header = dataFile.getHeader();
                    //final long timeEnd0 = System.nanoTime();
                    //System.out.println("Verlaufszeit für Header: " + (timeEnd0 - timeStart0)/(1000000000) + " Sek.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1002, null, e.getMessage(), null));
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO check statuscode
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1002, null, e.getMessage(), null));
                }
                cacheDataFile(id, dataFile);
            }else{
                try {
                    header = dataFile.getHeader();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            DataFileColumn timeStampColumn = null;
            DataFileColumn dataColumn = null;
            try {
//                final long timeStart = System.nanoTime();
                timeStampColumn = header.getColumn(timestamp);
//                final long timeEnd = System.nanoTime();
//                System.out.println("Verlaufszeit für TimeStampColumn: " + (timeEnd - timeStart)/(1000000000) + " Sek.");

                final long timeStart1 = System.nanoTime();
                dataColumn = header.getColumn(name);
                final long timeEnd1 = System.nanoTime();
//                System.out.println("Verlaufszeit für NameColumn: " + (timeEnd1 - timeStart1)/(1000000000) + " Sek.");
            } catch (FileException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(1004, null, e.getMessage(), null));
            }

            List<String> timeStamps = null;
            List<String> rawValues = null;
            try {
                //final long timeStart = System.nanoTime();
                timeStamps = DataFileUtils.getRawValues(dataFile, timeStampColumn, true);
                //final long timeEnd = System.nanoTime();
                //System.out.println("Verlaufszeit für TimeStamps: " + (timeEnd - timeStart)/(1000000000) + " Sek.");
                //final long timeStart1 = System.nanoTime();
                rawValues = DataFileUtils.getRawValues(dataFile, dataColumn, true);
                //final long timeEnd1 = System.nanoTime();
                //System.out.println("Verlaufszeit für ColumnValues: " + (timeEnd1 - timeStart1)/(1000000000) + " Sek.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < timeStamps.size(); i++) {
                GraphData graphData;
                if(timeStamps.get(i).trim().equals("")){
                    graphData = new GraphData(null, rawValues.get(i));
                }else {
                    graphData = new GraphData(Long.valueOf(timeStamps.get(i)), rawValues.get(i));
                }
                graphDataArray.add(graphData);
            }
            dataServiceInterface.addToList(id, name, timestamp, graphDataArray, userId);
        }
        List<GraphData> graphDataArrayToSend = null;
        if (start == null) {
            graphDataArrayToSend = graphDataArray;
        }else{
            graphDataArrayToSend = new ArrayList<>();
            for(GraphData data:graphDataArray){
                Long timestamp1 = data.getTimestamp();
                if(timestamp1.compareTo(stop)>0){
                    break;
                }
                if(timestamp1.compareTo(start)>=0){
                    graphDataArrayToSend.add(data);
                }

            }
        }

        return ResponseEntity.ok(new DataMsg(0, null, null, graphDataArrayToSend));
    }

    private void cacheDataFile(Long id, DataFile dataFile) {
        dataServiceInterface.addToFileCache(id, dataFile);
    }

    private DataFile getDataFile(Long id) {
        return dataServiceInterface.isInFileCache(id);
    }
}
