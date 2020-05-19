package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.domain.Notifications;
import com.dcap.domain.User;

import javax.transaction.Transactional;
import java.util.List;

public interface NotificationServiceInterface {
    List<Notifications> getNotificationByUser(User user);

    @Transactional(value = Transactional.TxType.REQUIRED)
    List<Notifications> getNotificationByUserAndMark(User user);

    Notifications safeOrUpdate(Notifications notification);

    void delete(Notifications notification);

    List<Notifications> getAllNotifications();
}
