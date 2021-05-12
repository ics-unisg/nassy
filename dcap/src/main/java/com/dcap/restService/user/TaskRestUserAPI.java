package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.TaskRestUser;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.TaskRestUser;
import com.dcap.transferObjects.EasyUserData;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class TaskRestUserAPI {

    @Autowired
    TaskRestUser taskRestUser;

    @ApiOperation(value = "Requests the userdata resulting from a task by task id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
//            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/taskid/{taskId}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<EasyUserData>> getUserDataByTaskId(@ApiParam(value = "Id of a given task.") @PathVariable("taskId") String taskId) {
        return this.taskRestUser.getUserDataByTaskId(taskId);
    }


    @ApiOperation(value = "Checks if a task given by an id has finished.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/taskFinished/{taskId}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<Boolean>> checkIfTaskFinished(@ApiParam(value = "Id of a given task.") @PathVariable("taskId") String taskId) {
        return this.taskRestUser.checkIfTaskFinished(taskId);
    }
}
