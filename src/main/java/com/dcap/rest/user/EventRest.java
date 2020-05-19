package com.dcap.rest.user;

import com.dcap.domain.ENUMERATED_CATEGORIES;
import com.dcap.domain.Event;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.domain.ENUMERATED_CATEGORIES;
import com.dcap.domain.Event;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
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
public class EventRest {

    final UserServiceInterface userServiceInterface;
    final UserDataServiceInterface userDataServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public EventRest(UserServiceInterface userServiceInterface, UserDataServiceInterface userDataServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {
        this.userServiceInterface = userServiceInterface;
        this.userDataServiceInterface = userDataServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<ArrayList<Event>>>getEvents(Long dataId) {
        User currentUser = mySecurityAccessor.getCurrentUser();

        UserData userDataById = null;
        try {
            userDataById = userDataServiceInterface.getUserDataById(dataId);
        } catch (RepoExeption exeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new DataMsg<>(300, null, "No such file", null));
        }
        if(!userDataById.getUser().getId().equals(currentUser.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataMsg(102, null, "User does not belong the data.", null));
        }
        if (userDataById.getCategories() != ENUMERATED_CATEGORIES.EVENTS) {
            return ResponseEntity.ok(new DataMsg(106, null, "The userdata with id " + dataId + " are not events.", null));
        }
        ArrayList<Event> events = new ArrayList<>();
        String path = userDataById.getPath();
        File file = new File(path);
        DataFile dataFile = null;
        try {
            dataFile = new DataFile(file,path, ",", true, ".");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(102, null, e.getMessage(), null));
        }
        DataFileColumn startTimeStamp;
        DataFileColumn endTimeStamp;
        DataFileColumn type;
        DataFileColumn attributes;
        DataFileColumn name;
        try {
            startTimeStamp = dataFile.getHeader().getColumn("startTimeStamp");
            endTimeStamp = dataFile.getHeader().getColumn("endTimeStamp");
            type = dataFile.getHeader().getColumn("type");
            name = dataFile.getHeader().getColumn("name");
            attributes = dataFile.getHeader().getColumn("attributes");
        } catch (FileException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(105, null, e.getMessage(), null));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(201, null, e.getMessage(), null));
        }
        Long[] timeStampsStartList = null;
        Long[] timeStampsEndList = null;
        List<String> nameList = null;
        List<String> tpyeList = null;
        List<String> attributeList = null;
        try {
            timeStampsStartList = DataFileUtils.getTimeStamps(dataFile, startTimeStamp, true);
            timeStampsEndList = DataFileUtils.getTimeStamps(dataFile, endTimeStamp, true);
            nameList = DataFileUtils.getRawValues(dataFile, name, true);
            tpyeList = DataFileUtils.getRawValues(dataFile, type, true);
            attributeList = DataFileUtils.getRawValues(dataFile, attributes, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i <timeStampsStartList.length; i++){
            Event event = new Event(nameList.get(i), tpyeList.get(i), timeStampsStartList[i], timeStampsEndList[i], attributeList.get(i));
            events.add(event);
        }
        return ResponseEntity.ok(new DataMsg<>(0, null, null, events));
    }
}
