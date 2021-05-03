package com.dcap.restService.admin;


import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.DataProcessingRestAdmin;
import com.dcap.transferObjects.EasyDataProcessing;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.DataProcessingRestAdmin;
import com.dcap.transferObjects.EasyDataProcessing;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/admin")
public class DataProcessingRestAdminAPI {

    @Autowired
    DataProcessingRestAdmin dataProcessingRestAdmin;

    @ApiOperation(value = "Receive the dataprocessing for the given id.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class) })
    @RequestMapping(value = "/getdataprocessing/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<EasyDataProcessing>> getDataProcessingById(
            @ApiParam(name = "id", value = "Id of the dataprocessing", required = true) @PathVariable("id") Long id) {
        return this.dataProcessingRestAdmin.getDataProcessingById(id);
    }
}
