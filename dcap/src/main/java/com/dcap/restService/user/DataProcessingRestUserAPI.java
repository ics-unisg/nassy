package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.DataProcessingRestUser;
import com.dcap.transferObjects.DataProcessingRequest;
import com.dcap.transferObjects.EasyDataProcessing;
import com.dcap.transferObjects.EasyDataProcessingStoreObject;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.DataProcessingRestUser;
import com.dcap.transferObjects.DataProcessingRequest;
import com.dcap.transferObjects.EasyDataProcessing;
import com.dcap.transferObjects.EasyDataProcessingStoreObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/user")
public class DataProcessingRestUserAPI {

     @Autowired
     DataProcessingRestUser dataProcessingRestUser;

    @ApiOperation(value = "Downloads dataprocessing with the given ID.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/getdataprocessing/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<EasyDataProcessing>> getDataProcessingById(@PathVariable("id") Long id) {
        return this.dataProcessingRestUser.getDataProcessingById(id);
    }

    @ApiOperation(value = "Save a dataprocessing to the tool.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class) })
    @RequestMapping(value = "/savedataprocessing", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<EasyDataProcessing>> saveDataProcessing(@ApiParam(name = "easyDataProcessingStoreObject", value = "Dataprocessing to store", required = true) @RequestBody EasyDataProcessingStoreObject easyDataProcessingStoreObject) {
        return this.dataProcessingRestUser.saveDataProcessing(easyDataProcessingStoreObject);
    }

    @ApiOperation(value = "Apply a dataprocessing to given files.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class) })
    @RequestMapping(value = "/usedataprocessing", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<String>> useDataProcessing(@ApiParam(name = "dataProcessingRequest", value = " The dataprocessing id and the files to be treated", required = true) @RequestBody DataProcessingRequest dataProcessingRequest){
        return this.dataProcessingRestUser.useDataProcessing(dataProcessingRequest);
    }

    }
