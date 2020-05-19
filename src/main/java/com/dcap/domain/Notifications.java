package com.dcap.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * This class describes the notifications a user got
 *
 * @author uli
 */

@Entity
public class Notifications implements DatabaseEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_notification")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_user")
    private User user;

    private String message;
    private String type;
    private String url;
    private boolean is_read;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date timestamp;
    @Column(name = "task_id")
    private String taskId;

    /**
     * default constructor
     */
    public Notifications() {
    }


    /**
     * Constructor for the class
     *  @param user user, the notifications are given
     * @param message message, that describes the notification
     * @param type status (success,...)
     * @param url link, a notification can point to
     * @param is_read notification is read or not
     * @param timestamp time a notification was created
     * @param taskId id for the identifying task by iid;
     */
    public Notifications(User user, String message, String type, String url, boolean is_read, Date timestamp, String taskId) {
        this.user = user;
        this.message = message;
        this.type = type;
        this.url = url;
        this.is_read = is_read;
        this.timestamp = timestamp;
        this.taskId = taskId;
    }

    /**
     * The id as in the database
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notifications that = (Notifications) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
