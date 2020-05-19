package com.dcap.service.threads;

import com.dcap.domain.UserData;
import java.util.Objects;


/**
 * Class to transport messages from thread to main thread
 *
 * @author uli
 */
public class ThreadResponse {
    private String type;
    private String message;
    private String path;
    private String name;
    private Long userId;
    private UserData userData;
    private String notification;
    private String id;

    public ThreadResponse(String id, String type, String message, String path, String name, Long userId, UserData userData) {
        this.type = type;
        this.message = message;
        this.path = path;
        this.name = name;
        this.userId = userId;
        this.userData = userData;
        this.id=id;
    }

    public ThreadResponse(String id, String type, String message, String path, String name, Long userId, UserData userData, String notification) {
        this.type = type;
        this.message = message;
        this.path = path;
        this.name = name;
        this.userId = userId;
        this.userData = userData;
        this.notification = notification;
        this.id=id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThreadResponse)) return false;
        ThreadResponse that = (ThreadResponse) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(message, that.message) &&
                Objects.equals(path, that.path) &&
                Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userData.getId(), that.userData.getId()) &&
                Objects.equals(notification, that.notification) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, message, path, name, userId, userData.getId(), notification, id);
    }
}
