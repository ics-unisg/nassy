package com.dcap.domain;

import javax.persistence.*;

/**
 * This class describes some metainformations about the user-data
 *
 * @author uli
 *
 */

@Entity
@Table(name = "user_data_tags")
public class UserDataTags implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_user_data_tags")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fk_user_data")
    private UserData userdata;
    private String tag;

    /**
     * default constructor
     */
    public UserDataTags() {
    }

    /**
     * constructor of the class
     *
     * @param userdata userdata the tags belong to
     * @param tag further description
     */
    public UserDataTags(UserData userdata, String tag) {
        this.userdata = userdata;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getUserdata() {
        return userdata;
    }

    public void setUserdata(UserData userdata) {
        this.userdata = userdata;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataTags that = (UserDataTags) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
