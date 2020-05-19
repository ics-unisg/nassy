package com.dcap.repository;

import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import com.dcap.domain.Notifications;
import com.dcap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;
import java.util.Set;

public interface NotificationsInterface extends JpaRepository<Notifications, Long> {
    public List<Notifications> findByUser(User user);



    @Modifying
    @Transactional
    @Query(value = "update notifications set notifications.is_read=true where fk_user=:id", nativeQuery = true)
    public void setRead(@Param("id") Long id);

}
