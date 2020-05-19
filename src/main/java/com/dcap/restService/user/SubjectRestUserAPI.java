package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.SubjectRestUser;
import com.dcap.transferObjects.EasySubject;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.SubjectRestUser;
import com.dcap.transferObjects.EasySubject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/user")
public class SubjectRestUserAPI {

    @Autowired
    SubjectRestUser subjectRestUser;


    @ApiOperation(value = "Gets all subjects of the given user.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value="/subjects", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasySubject>>> getSubjects() {
        return subjectRestUser.getSubjects();
    }



    @ApiOperation(value = "Gets all subjects for a given study.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/getsubjectsforstudy/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasySubject>>> getSubjectsForStudy(@ApiParam(value = "Id of the study which keeps the subjects.", required = true)@PathVariable("id") Long studyId){
        return this.subjectRestUser.getSubjectsForStudy(studyId);
    }


    @ApiOperation(value = "Gets subject for given id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/getsubject/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasySubject>>> getSubject(@ApiParam(value = "Id of the subjects.", required = true)@PathVariable("id") Long subjectId){
        return this.subjectRestUser.getSubject(subjectId);
    }

    @ApiOperation(value = "Saves a new subject.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
//            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/addsubject", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<EasySubject>> addSubject(@ApiParam(value = "Subject that should be added.", required = true) @RequestBody EasySubject subject){
        return this.subjectRestUser.addSubject(subject);
    }


    @ApiOperation(value = "Deletes a subject for the given id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class)
//            @ApiResponse(code = 403, message = "Forbidden", response = void.class)
    //        @ApiResponse(code = 404, message = "Not Found", response = void.class)
    })
    @RequestMapping(value = "/deletesubject/{subjectId}", method = RequestMethod.DELETE)
    public ResponseEntity<DataMsg> deleteSubject(@ApiParam(value = "Subject that should be deleted.", required = true) @PathVariable("subjectId") Long subjectId){
        return this.subjectRestUser.deleteSubject(subjectId);
    }


    @ApiOperation(value = "Updates a given subject.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
//            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "updatesubject", method = RequestMethod.PUT)
    public ResponseEntity<DataMsg<EasySubject>> updateSubject(@ApiParam(value = "Subject that should be updated.", required = true) @RequestBody EasySubject subject){
        return this.subjectRestUser.updateSubject(subject);
    }


    }
