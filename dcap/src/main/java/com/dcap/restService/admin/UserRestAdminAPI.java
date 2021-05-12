package com.dcap.restService.admin;


import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.UserRestAdmin;
import com.dcap.transferObjects.EasyUser;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.UserRestAdmin;
import com.dcap.transferObjects.EasyUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class UserRestAdminAPI {

    @Autowired
    UserRestAdmin userRestAdmin;

    @ApiOperation(value = "Create a new User.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<DataMsg<EasyUser>> creatUser(@ApiParam(value = "User that should be added.") @RequestBody EasyUser user) {
        ResponseEntity<DataMsg<EasyUser>> newUser = userRestAdmin.createNewUser(user);
        return newUser;
    }

    @ApiOperation(value = "Delete user by given user-id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DataMsg<Boolean>> deleteUser(@ApiParam(value = "Id of the user to delete", required = true) @PathVariable("id") Long id){
        return userRestAdmin.deleteUser(id);
    }


    @ApiOperation(value = "Get all users of the tool.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyUser>>> getAllUsers() {
        ResponseEntity<DataMsg<List<EasyUser>>> allUsers = userRestAdmin.getAllUsers();
        return allUsers;
    }

    @ApiOperation(value = "Get a user for a given id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyUser>>> findUser(@ApiParam(value = "Id of the user to find", required = true) @PathVariable("id") Long id) {
        ResponseEntity<DataMsg<List<EasyUser>>> userById = userRestAdmin.findUserById(id);
        return userById;
    }

    @ApiOperation(value = "Resets password of a user given by id. Returns a randomly generated password.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DataMsg.class)})
    @RequestMapping(value = "/user/password/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<String>> resetPassword(@ApiParam(value = "Id of the user", required = true) @PathVariable("id") Long id) {
        return userRestAdmin.resetPassword(id);
    }


    }
