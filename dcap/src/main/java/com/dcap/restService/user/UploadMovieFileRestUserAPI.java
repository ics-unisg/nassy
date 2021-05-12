package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.UploadMovieFileRestUser;
import com.dcap.rest.responses.FileUploadResponse;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.UploadMovieFileRestUser;
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
public class UploadMovieFileRestUserAPI {

    @Autowired
    UploadMovieFileRestUser uploadMovieFileRestUser;


    @ApiOperation(value = "Uploads a list of movie-files (webm) for saving. To use the automated attaching of files to subject, please name the " +
            "files in the form \"{subject-name}@{study-name}@{file-name}.webm\". The subject-name and the study-name must be already in the database.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 415, message = "Unsupported Media Type", response = DataMsg.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @PostMapping("/uploadMovieFile")
    public ResponseEntity<DataMsg<FileUploadResponse>> handleFileUpload(
            @ApiParam(value = "List of movies that should be safed.", required = true)@RequestParam("files") List<MultipartFile> files,
            @ApiParam(value = "List of userdata-ids that the movies should be attached.", required = true)@RequestParam("subjectIds")  List<Long> subjectIds) {
        return this.uploadMovieFileRestUser.handleFileUpload(files, subjectIds);
    }

}
