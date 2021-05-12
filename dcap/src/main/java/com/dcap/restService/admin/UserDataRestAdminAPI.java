package com.dcap.restService.admin;

import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.UserDataRestAdmin;
import com.dcap.transferObjects.EasyUserData;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.UserDataRestAdmin;
import com.dcap.transferObjects.EasyUserData;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/admin")
public class UserDataRestAdminAPI {

    @Autowired
    UserDataRestAdmin userDataRestAdmin;

    @ApiOperation(value = "Retrieve a userdata for a given user id.",
//            notes = "",
            authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/userdata/{userId}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<EasyUserData>> getUserData(@ApiParam(value = "The id of the user.", required = true) @PathVariable("userId") Long userId) {
        return userDataRestAdmin.getUserData(userId);
    }
}
