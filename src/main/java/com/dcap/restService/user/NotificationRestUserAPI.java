package com.dcap.restService.user;

import com.dcap.rest.DataMsg;
import com.dcap.rest.user.NotificationRestUser;
import com.dcap.rest.DataMsg;
import com.dcap.rest.user.NotificationRestUser;
import com.dcap.transferObjects.EasyNotification;
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
public class NotificationRestUserAPI {

    @Autowired
    NotificationRestUser notificationRestUser;

    @ApiOperation(value = "Receives list of all notifications.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyNotification>>> getAllNotifications(){
        return notificationRestUser.getUsersNotifications();
    }
}
