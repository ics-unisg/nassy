package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.FilterDetailsRestUser;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.FilterDetailsRestUser.ParameterForMessage;
import com.dcap.rest.user.FilterDetailsRestUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class FilterDetailsRestUserAPI {

    @Autowired
    FilterDetailsRestUser filterDetailsRestUser;


    @ApiOperation(value = "Receives list of events from file.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/filterdetails/{filtername}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<ArrayList<FilterDetailsRestUser.ParameterForMessage>>> getFilterDetails(@PathVariable("filtername") String filterName) {
        ResponseEntity<DataMsg<ArrayList<FilterDetailsRestUser.ParameterForMessage>>> filterDetails = null;
        try {
            filterDetails = this.filterDetailsRestUser.getFilterDetails(filterName);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO find better handling
        }
        return filterDetails;
    }

    @RequestMapping(value = "/listoffilters", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<String>>> gitListOfFilters(){
        return this.filterDetailsRestUser.getListOfFilters();
    }

}
