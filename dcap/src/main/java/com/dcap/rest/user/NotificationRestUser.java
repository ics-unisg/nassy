package com.dcap.rest.user;

import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.rest.DataMsg;
import com.dcap.transferObjects.EasyNotification;
import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.repository.NotificationsInterface;
import com.dcap.rest.DataMsg;
import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.serviceInterfaces.NotificationServiceInterface;
import com.dcap.transferObjects.EasyNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationRestUser {

    final NotificationServiceInterface notificationService;
    final MySecurityAccessorInterface mySecurityAccessor;


    public NotificationRestUser(NotificationServiceInterface notificationService, MySecurityAccessorInterface mySecurityAccessor) {
        this.notificationService = notificationService;
        this.mySecurityAccessor = mySecurityAccessor;
    }

    public ResponseEntity<DataMsg<List<EasyNotification>>> getUsersNotifications(){
        User currentUser = mySecurityAccessor.getCurrentUser();
        List<Notifications> byUser = notificationService.getNotificationByUserAndMark(currentUser);
        List<EasyNotification> notiList = byUser.stream().map(e -> new EasyNotification(e)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new DataMsg<>(0,null, null,notiList));
    }




}
