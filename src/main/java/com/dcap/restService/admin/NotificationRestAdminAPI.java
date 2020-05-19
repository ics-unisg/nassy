package com.dcap.restService.admin;

import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.NotificationRestAdmin;
import com.dcap.transferObjects.EasyNotification;
import com.dcap.rest.DataMsg;
import com.dcap.rest.admin.NotificationRestAdmin;
import com.dcap.transferObjects.EasyNotification;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class NotificationRestAdminAPI {

    @Autowired
    NotificationRestAdmin notificationRestAdmin;

    @ApiOperation(value = "Receives list of all notifications.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyNotification>>> getAllNotifications(){
        return notificationRestAdmin.getAllNotifications();
    }

    @ApiOperation(value = "Receives notification with given id.",
//            notes = "",
            authorizations = {@Authorization(value = "basicAuth", scopes = {})})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = void.class)})
    @RequestMapping(value = "/notification/{id}", method = RequestMethod.GET)
    public ResponseEntity<DataMsg<List<EasyNotification>>> getAllNotificationsOfUser(@ApiParam(value = "Id of the notification") @PathVariable Long id){
        return notificationRestAdmin.getUsersNotifications(id);
    }


}
