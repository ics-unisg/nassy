package com.dcap.analyzer;

import com.dcap.domain.UserData;
import java.io.File;


/**
 *Class used to keep informations about data
 *
 * @author uli
 */
public class FileNameUser{
    private String subject;
    private String name;
    private File file;
    private String path;
    private UserData userData;

    public FileNameUser(String subject, String name, File file, String path, UserData ud) {
        this.subject = subject;
        this.name = name;
        this.file = file;
        this.path=path;
        this.userData = ud;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
