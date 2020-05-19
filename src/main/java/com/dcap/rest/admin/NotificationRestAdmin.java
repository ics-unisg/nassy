package com.dcap.rest.admin;

import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.EasyNotification;
import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.NotificationServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.transferObjects.EasyNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationRestAdmin {


    final NotificationServiceInterface notificationService;
    final UserServiceInterface userServiceInterface;


    public NotificationRestAdmin(NotificationServiceInterface notificationService, UserServiceInterface userServiceInterface) {
        this.notificationService = notificationService;
        this.userServiceInterface = userServiceInterface;
    }

    public ResponseEntity<DataMsg<List<EasyNotification>>> getUsersNotifications(Long userId){
        User user = null;
        try {
            user = userServiceInterface.findUserById(userId);
        } catch (RepoExeption repoExeption) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(301,null, repoExeption.getMessage(),null));}
        List<Notifications> byUser = notificationService.getNotificationByUser(user);
        List<EasyNotification> notiList = byUser.stream().map(e -> new EasyNotification(e)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new DataMsg<>(0,null, null,notiList));
    }

    public ResponseEntity<DataMsg<List<EasyNotification>>> getAllNotifications(){
        List<Notifications> byUser = notificationService.getAllNotifications();
        List<EasyNotification> notiList = byUser.stream().map(e -> new EasyNotification(e)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new DataMsg<>(0,null, null,notiList));
    }

}
