package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.FilterRestUser;
import com.dcap.transferObjects.EasyFilterRequest;
import com.dcap.transferObjects.MeasureRequest;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.FilterRestUser;
import com.dcap.transferObjects.EasyFilterRequest;
import com.dcap.transferObjects.MeasureRequest;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/user")
public class FilterRestUserAPI {


    @Autowired
    private FilterRestUser filterRest;


    @ApiOperation(value = "Initiates the filtering of  one ore more files",
            notes = "Returns the ID of the task",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/filterrequest", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<String>> makeFilterRequest(@ApiParam(name = "filter", value = "List of the files and list of filters, the columns and the parameter are key-value pairs with name for the tool and the value.", required = true) @RequestBody EasyFilterRequest filter) {
        return filterRest.makeFilterRequest(filter);
    }

    @ApiOperation(value = "Initiates the filtering of  one ore more files",
            notes = "Returns the ID of the task",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/calculatemeasures", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<String>> calculateMeasure(@RequestBody MeasureRequest request) {
        return filterRest.calculateMeasure(request);
    }
}
