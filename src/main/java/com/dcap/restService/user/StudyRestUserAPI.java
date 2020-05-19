package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.StudyRestUser;
import com.dcap.transferObjects.EasyStudy;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.StudyRestUser;
import com.dcap.transferObjects.EasyStudy;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class StudyRestUserAPI {

    @Autowired
    StudyRestUser studyRestUser;

    @ApiOperation(value = "Lists all studies of the current user.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value="/studies", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyStudy>>> getAllStudies(){
        return studyRestUser.getAllStudiesForUser();
    }

    @ApiOperation(value = "Saves changes to a given study.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/study", method = RequestMethod.PUT)
    public ResponseEntity<DataMsg<EasyStudy>> changeStudy(@ApiParam(value = "Study that should be updated.", required = true) @RequestBody EasyStudy easyStudy){
        return studyRestUser.changeStudy(easyStudy);
    }


    @ApiOperation(value = "Saves the given study to the current user.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/study", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<EasyStudy>> addStudy(@ApiParam(value = "Study that should be saved (It is not necessary to set the field userId).", required = true) @RequestBody EasyStudy study){
        return studyRestUser.saveStudy(study);
    }

    @ApiOperation(value = "Delete given study",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/study", method = RequestMethod.DELETE)
    public ResponseEntity<DataMsg<Boolean>> deleteStudy(@ApiParam(value = "Study that should be deleted (It is not necessary to set the field userId).", required = true) @RequestBody EasyStudy easyStudy){
        return studyRestUser.deleteStudy(easyStudy);
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
        return studyRestUser.deleteStudy(id);
    }

    @ApiOperation(value = "Gets all study of the given user.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value="/study/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<EasyStudy>> getStudy(@ApiParam(value = "Study that should be retrieved.", required = true) @PathVariable("id") Long id) {
        return studyRestUser.getStudy(id);
    }



}
