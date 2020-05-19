package com.dcap.rest.admin;

import com.dcap.domain.Pythoncode;
import com.dcap.rest.DataMsg;
import com.dcap.service.storage.StorageService;
import com.dcap.transferObjects.EasyPythoncode;
import com.dcap.domain.Pythoncode;
import com.dcap.domain.User;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.Exceptions.PythoncodeException;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.PythonCodeService;
import com.dcap.service.storage.StorageService;
import com.dcap.transferObjects.EasyPythoncode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PythonRestAdmin {

    private final StorageService storageService;
    private final PythonCodeService pythonCodeRepo;


    public PythonRestAdmin(StorageService storageService, PythonCodeService pythonCodeRepo) {
        this.storageService = storageService;
        this.pythonCodeRepo = pythonCodeRepo;
    }

    public ResponseEntity<DataMsg<EasyPythoncode>> handleCodeUpload(List<MultipartFile> files, String name, String type) {
        String parameteradress;
        String codeadress;
        MultipartFile paramfile = null;
        MultipartFile pyfile = null;

        if (files.size() != 2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(401, null, "One Python file and one" +
                    "python-file or parameter-file is needed.", null));
        }
        for (MultipartFile file : files) {
            if (file.getOriginalFilename().endsWith("param")) {
                paramfile = file;
            }
            if (file.getOriginalFilename().endsWith("py")) {
                pyfile = file;
            }
        }

        if (paramfile == null || pyfile == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(401, null, "One Python file and one" +
                    "python-file or parameter file is needed.", null));
        }

        if(type!=null &&!type.trim().equals("trim") && !type.trim().equals("custom") ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(402, null, "File has wrong type", null));
        }

        if(pythonCodeRepo.getPythonCodeElement(name)!=null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(403, null, "Name already exists.", null));
        }

        parameteradress = storageService.store(paramfile, "python").getKey();
        codeadress = storageService.store(pyfile, "pyhton").getKey();
        Pythoncode pythonCodeToStore = null;
        try {
            pythonCodeToStore = new Pythoncode(name, parameteradress, codeadress, type);
        } catch (PythoncodeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(402, null, e.getMessage(), null));}
        Pythoncode pythoncode;
        try {
            pythoncode = pythonCodeRepo.saveCode(pythonCodeToStore);
        } catch (PythoncodeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(403, null, e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new DataMsg<>(0, null, null, new EasyPythoncode(pythoncode)));
    }

    public ResponseEntity<DataMsg<Boolean>> deletePythonCode(String name){
        boolean deleted = false;
        try {
            deleted = pythonCodeRepo.deletePythonCode(name);
        } catch (PythoncodeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(0, null, e.getMessage(), null));

        }
        if(deleted){
            return ResponseEntity.status(HttpStatus.OK).body(new DataMsg<>(0, null, null, true));
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(404, null, "Could not delete file", false));

        }

    }

}
