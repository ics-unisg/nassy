package com.dcap.service;

import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.repository.NotificationsInterface;
import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.repository.NotificationsInterface;
import com.dcap.service.serviceInterfaces.NotificationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Service to manage the Notifications in the database
 *
 * @author uli
 */

@Service
public class NotificationService implements NotificationServiceInterface {

    private final NotificationsInterface notificationsRepo;

    @Autowired
    public NotificationService(NotificationsInterface notificationsRepo) {
        this.notificationsRepo = notificationsRepo;
    }

    /**
     * Get a list of all Notifications in the database
     * @param user User who is the ownwer of the notifications
     * @return list of Notifications for a given User
     */
    @Override
    public List<Notifications> getNotificationByUser(User user){
        return notificationsRepo.findByUser(user);
    }


    /**
     *Get a list of all Notifications in the database and sets the value of read to true.
     * @param user User who is the ownwer of the notifications
     * @return list of Notifications for a given User
     */
    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public List<Notifications> getNotificationByUserAndMark(User user){
        notificationsRepo.setRead(user.getId());
        return notificationsRepo.findByUser(user);
    }

    /**
     *saves or updates Notifications items in the database
     * @param notification Notification item to be saved or updated
     * @return Notification item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public Notifications safeOrUpdate(Notifications notification){
        return  notificationsRepo.save(notification);
    }

    /**
     * deletes given Notification in the database
     * @param notification notification to be deleted
     */
    @Override
    public void delete(Notifications notification) {
        notificationsRepo.delete(notification);
    }

    @Override
    public List<Notifications> getAllNotifications() {
        List<Notifications> all = notificationsRepo.findAll();
        return all;
    }


}
