package com.dcap.rest.admin;

import com.dcap.domain.UserData;
import com.dcap.rest.DataMsg;
import com.dcap.domain.UserData;
import com.dcap.rest.DataMsg;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileRestAdmin {

    final UserDataServiceInterface userDataServiceInterface;

    @Autowired
    public FileRestAdmin(UserDataServiceInterface userDataServiceInterface) {
        this.userDataServiceInterface = userDataServiceInterface;
    }



    public ResponseEntity<Resource> download( String fileid) throws IOException, RepoExeption {

        UserData userDataById = userDataServiceInterface.getUserDataById(Long.valueOf(fileid));

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(userDataById.getPath())));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userDataById.getFilename() + "\"")
                .body(resource);
    }

    public ResponseEntity<DataMsg> clean(String confirm) {
        if(confirm.equals("yes")){
            userDataServiceInterface.cleanAll();
            return ResponseEntity.ok(new DataMsg(0, null, null, "All files deleted"));


        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(201, null, null, "You didn't say yes..."));
    }
}

