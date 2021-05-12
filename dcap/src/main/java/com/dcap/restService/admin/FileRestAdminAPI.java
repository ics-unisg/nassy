package com.dcap.restService.admin;


import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.FileRestAdmin;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.FileRestAdmin;
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
@RequestMapping("/api/admin")
public class FileRestAdminAPI{

    @Autowired
    FileRestAdmin fileRestAdmin;


    @ApiOperation(value = "Download a file with a given id.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class) })
    @RequestMapping(path = "/download/{fileid}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@ApiParam(value = "Id of the desired file.") @PathVariable("fileid") String fileid) throws IOException, RepoExeption {
        return this.fileRestAdmin.download(fileid);
    }

    @ApiOperation(value = "Clean database. Deletes all entries with hidden == true and deletes files from filesystem. CANNOT BE UNDONE",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class) })
    @RequestMapping(path = "/clean/{confirm}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg> clean(@ApiParam(value = "Are you sure?") @PathVariable("confirm") String confirm) throws IOException, RepoExeption {
        return this.fileRestAdmin.clean(confirm);
    }


}
