package com.dcap.rest.user;

import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileRestUser {

    final UserDataServiceInterface userDataServiceInterface;
    final MySecurityAccessorInterface mySecurityAccessor;

    @Autowired
    public FileRestUser(UserDataServiceInterface userDataServiceInterface, MySecurityAccessorInterface mySecurityAccessor) {
        this.userDataServiceInterface = userDataServiceInterface;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<Resource> download(String fileid) throws IOException, FileException {
        User user = mySecurityAccessor.getCurrentUser();
        UserData userDataById = null;
        try {
            userDataById = userDataServiceInterface.getUserDataById(Long.valueOf(fileid));
        } catch (RepoExeption exeption) {
            throw new FileException(exeption.getMessage());
        }

        if(!userDataById.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("File not owned by user");
        }

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(userDataById.getPath())));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userDataById.getFilename() + "\"")
                .body(resource);
    }

    public ResponseEntity<DataMsg> delete(Long fileid) throws IOException, RepoExeption {
        User user = mySecurityAccessor.getCurrentUser();
        UserData userDataById = userDataServiceInterface.getUserDataById(fileid);

        if(!userDataById.getUser().getId().equals(user.getId())){
            //throw new AccessDeniedException("File not owned by user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataMsg(1101, null,"File not owned by user", null));

        }

        userDataServiceInterface.delete(userDataById);
        return ResponseEntity.ok(new DataMsg(0, null, null, "True"));

    }

}

