package com.dcap.restService.admin;

import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.StudyRestAdmin;
import com.dcap.transferObjects.EasyStudy;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.StudyRestAdmin;
import com.dcap.transferObjects.EasyStudy;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class StudyRestAdminAPI {

    @Autowired
    StudyRestAdmin studyRestAdmin;


    @ApiOperation(value = "Get all studies of a user via given user-id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/studies/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyStudy>>> getAllStudieOfUser(
            @ApiParam(value = "Id of the user", required = true) @PathVariable("id") Long id){
        return studyRestAdmin.getStudiesOfUser(id);
    }

    @ApiOperation(value = "Delete study with given id",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/study/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DataMsg<Boolean>> deleteStudy(@ApiParam(value = "Study that should be deleted.", required = true) @PathVariable("id") Long id){
        return studyRestAdmin.deleteStudy(id);
    }
}
