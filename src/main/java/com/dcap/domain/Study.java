package com.dcap.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * This class describes the studies
 *
 * @author uli
 *
 */

@Entity
@Table(name="studies")
public class Study implements DatabaseEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_study")
    private Long id;
    private String name;
    @Lob
    private String comment;

    private Long synchronized_from;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user")
    private User user;

    public Study() {
    }


    public Study(String name, String comment, Long synchronized_from, User user) {
        this.name = name;
        this.comment = comment;
        this.synchronized_from = synchronized_from;
        this.user=user;
    }

    /**
     * The id as in the database
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getSynchronized_from() {
        return synchronized_from;
    }

    public void setSynchronized_from(Long synchronized_from) {
        this.synchronized_from = synchronized_from;
    }

    public User getUsers() {
        return user;
    }

    public void setUsers(User user) {
        this.user = user;
    }

    public void removeUser() {
        this.user=null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Study)) return false;
        Study study = (Study) o;
        return Objects.equals(id, study.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public void setId(Long o) {
        this.id=o;
    }

    public void setName(String name) {
        this.name=name;
    }
}
