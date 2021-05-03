package com.dcap.domain;

import javax.persistence.*;

/**
 * This class describes the User-data, which are data a user stores.
 * Normaly, every User-data has a subject to whom the data belong
 *
 *@author uli
 *
 */


@Entity
@Table(name="user_data")
public class UserData implements DatabaseEntryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_user_data")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_subject")
    private Subject subject;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_derived_from")
    private UserData derived;
    private String filename;
    private String type;
    private String path;
    private boolean hidden;
    @Lob
    private String comment;
    @Column(name = "start_timestamp")
    private Long startTimestamp;
    @Enumerated(EnumType.STRING)
    private ENUMERATED_CATEGORIES categories;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user")
    private User user;
    @Column(name = "task_id")
    private String taskId;


    /**
     * default constructor
     */
    public UserData() {
    }

    /**
     *  Constructor of the class
     * @param subject the subject that is the source of the data
     * @param derived the parent User-data
     * @param filename
     * @param type type of data
     * @param path location on the disk where the data are stored
     * @param hidden defines if the data are deleted (=hidden) or not
     * @param comment comments and information for the data
     * @param startTimestamp start timestamp according to a file, if this is a video
     * @param category defining weather it is a video or a eventFile or a datafile
     * @param user user that is the owner of the userdata
     * @param taskId id of the task if it is added via a worker, used to identify it after task was added
     */

    public UserData(Subject subject, UserData derived, String filename, String type, String path, boolean hidden, String comment, Long startTimestamp, ENUMERATED_CATEGORIES category, User user, String taskId) {
        this.subject = subject;
        this.derived = derived;
        this.filename = filename;
        this.type = type;
        this.path = path;
        this.hidden = hidden;
        this.comment = comment;
        this.startTimestamp = startTimestamp;
        this.categories = category;
        this.user = user;
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public UserData getDerived() {
        return derived;
    }

    public void setDerived(UserData derived) {
        this.derived = derived;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public ENUMERATED_CATEGORIES getCategories() {
        return categories;
    }

    public void setCategories(ENUMERATED_CATEGORIES categories) {
        this.categories = categories;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

        UserData userData = (UserData) o;

        return id.equals(userData.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
