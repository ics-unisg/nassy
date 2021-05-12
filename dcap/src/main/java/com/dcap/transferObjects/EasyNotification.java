package com.dcap.transferObjects;

import com.dcap.domain.Notifications;
import com.dcap.domain.User;

import javax.persistence.*;
import java.util.Date;

public class EasyNotification {

    private Long id;


    private Long userId;

    private String message;
    private String type;
    private String url;
    private boolean is_read;
    private java.util.Date timestamp;
    private String taskId;

    public EasyNotification(Notifications notification) {
        this.id = notification.getId();
        this.userId = notification.getUser().getId();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.url = notification.getUrl();
        this.is_read = notification.isIs_read();
        this.timestamp = notification.getTimestamp();
        this.taskId = notification.getTaskId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
