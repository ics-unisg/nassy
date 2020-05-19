package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.DataRestUser;
import com.dcap.transferObjects.GraphData;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.DataRestUser;
import com.dcap.transferObjects.GraphData;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
@RequestMapping("/api/user")
public class DataRestUserAPI {

    @Autowired
    DataRestUser dataRestUser;

    //TODO check the start stop in the sense of, what is included
    @ApiOperation(value = "Receive data of a given column in a timerange.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "data/{id}/{timestamp}/{name}/{start}/{stop}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<ArrayList<GraphData>>> getDataForTime(
            @ApiParam(value = "ID of the datafile.", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Name of the timestampcolumn.", required = true) @PathVariable("timestamp") String timestamp,
            @ApiParam(value = "Name of the column with the desired values") @PathVariable("name") String name,
            @ApiParam(value = "Start of the timeslot.", required = true) @PathVariable("start") Long start,
            @ApiParam(value = "End of the timeslot", required = true) @PathVariable("stop") Long stop) {
        return this.dataRestUser.getDataForTime(id, timestamp, name, start, stop);
    }

    @ApiOperation(value = "Receive all data of a given column.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "data/{id}/{timestamp}/{name}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<ArrayList<GraphData>>> getDataForTimeCut(
            @ApiParam(value = "ID of the datafile.", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Name of the timestampcolumn.", required = true) @PathVariable("timestamp") String timestamp,
            @ApiParam(value = "Name of the column with the desired values") @PathVariable("name") String name
    ) {
        return this.dataRestUser.getDataForTimeCut(id, timestamp, name);
    }


}
