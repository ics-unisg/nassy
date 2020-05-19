package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.UserRestUser;
import com.dcap.restService.PWD;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.UserRestUser;
import com.dcap.restService.PWD;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/user")
public class UserRestUserAPI {

    @Autowired
    UserRestUser userRestUser;

    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @ApiOperation(value = "Updates password",  authorizations = {@Authorization(value="basicAuth", scopes = {})})
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<Boolean>> updatePassword(@ApiParam(name = "pwd", value = "New password", required = true) @RequestBody PWD pwd){
        return userRestUser.updatePasswort(pwd.getPwd());
    }

}
