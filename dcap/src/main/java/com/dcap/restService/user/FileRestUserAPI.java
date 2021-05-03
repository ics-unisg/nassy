package com.dcap.restService.user;


import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.FileRestUser;
import com.dcap.helper.FileException;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.FileRestUser;
import com.dcap.service.Exceptions.RepoExeption;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("/api/user")
public class FileRestUserAPI {

    @Autowired
    FileRestUser fileRestUser;


    @ApiOperation(value = "Downloads the with the given ID.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class) })
    @RequestMapping(path = "/download/{fileid}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@ApiParam(name = "fileid", value = "Id of the file to download", required = true) @PathVariable("fileid") String fileid) throws IOException, FileException {
        return this.fileRestUser.download(fileid);
    }

    @ApiOperation(value = "Delete file with given id",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/file/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DataMsg> deleteFile(@ApiParam(value = "File that should be deleted.", required = true) @PathVariable("id") Long id) throws IOException, RepoExeption {
        return fileRestUser.delete(id);
    }

}
