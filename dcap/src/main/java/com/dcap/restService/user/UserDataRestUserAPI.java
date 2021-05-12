package com.dcap.restService.user;


import com.dcap.rest.DataMsg;
import com.dcap.rest.user.UserDataRestUser;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.UserDataRestUser;
import com.dcap.transferObjects.EasyUserData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserDataRestUserAPI {

    @Autowired
    UserDataRestUser userDataRestUser;

    @ApiOperation(value = "Retrieves list of all userdata for the given user.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/userdataall",method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyUserData>>> getUserDataAll() {
        return this.userDataRestUser.getUserDataAll();
    }

}
