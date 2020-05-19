package com.dcap.restService.admin;

import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.PythonRestAdmin;
import com.dcap.transferObjects.EasyPythoncode;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.PythonRestAdmin;
import com.dcap.transferObjects.EasyPythoncode;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class PythonRestAdminAPI {

    @Autowired
    PythonRestAdmin pythonRestAdmin;

    @ApiOperation(value = "Upload a python file for the usage of the users.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/uploadpython", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<EasyPythoncode>> handleFileUpload(
            @ApiParam(value = "File with the code (*.py) and file with the parameter (*.param).", required = true) @RequestParam("files") List<MultipartFile> files,
            @ApiParam(value = "Name of the filter.", required = true)@RequestParam("name") String name,
            @ApiParam(value = "Type of the file/filter (\"trim\" or \"custom\")", required = true) @RequestParam("type") String type ) {
        return this.pythonRestAdmin.handleCodeUpload(files, name, type);
    }

    @ApiOperation(value = "Delete a python code by given name.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "deletepython/{name}",method = RequestMethod.DELETE )
    public ResponseEntity<DataMsg<Boolean>> deleteFile(@ApiParam(value = "Name of the file that should be deleted", required = true) @PathVariable("name") String name){
        return  this.pythonRestAdmin.deletePythonCode(name);
    }


}
