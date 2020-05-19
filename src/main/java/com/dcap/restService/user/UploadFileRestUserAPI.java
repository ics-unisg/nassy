package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.rest.user.UploadFileRestUser;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.UploadFileRestUser;
import com.dcap.rest.responses.FileUploadResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UploadFileRestUserAPI {

    @Autowired
    UploadFileRestUser uploadFileRestUser;

    @ApiOperation(value = "Uploads a list of data-files (tsv or csv) for saving. To use the automated attaching of files to subject, please name the files in the form \"{subject-name}@{study-name}@{file-name}.[tsv|csv]\". The subject-name and the study-name must be already in the database.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 415, message = "Unsupported Media Type", response = DataMsg.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @PostMapping("/uploadFile")
    public ResponseEntity<DataMsg<FileUploadResponse>> handleFileUpload(
            @ApiParam(value = "List of files that should be safed.", required = true) @RequestParam("files") List<MultipartFile> files,
            @ApiParam(value = "List of users the files should be attached to.", required = true)@RequestParam("subjectIds") List<Long> subjectIds) {
        return this.uploadFileRestUser.handleFileUpload(files, subjectIds);
    }


}
