package com.dcap.restService.user;


import com.dcap.domain.Event;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.EventRest;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

//@Controller
//@RequestMapping("/api/user")
public class EventRestUserAPI {

    @Autowired
    EventRest eventRest;

    @ApiOperation(value = "Receives list of events from file.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "getevents/{dataId}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<ArrayList<Event>>> getEvents(@ApiParam(value = "Id of datafile", required = true) @PathVariable("dataId") Long dataId) {
        return this.eventRest.getEvents(dataId);
    }
}
