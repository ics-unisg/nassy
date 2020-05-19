package com.dcap.domain;

import javax.persistence.*;
import javax.transaction.Transactional;

/**
 * This class describes a subject. A subject has a name and some other metadata to identify and contact the object
 * All subjects have a study thy are attached to
 * @author uli
 */

@Entity
@Table(name = "subjects")
public class Subject implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_subject")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_study")
    private Study study;
    private String email;
    @Column(name = "subject_id")
    private String subjectId;
    @Lob
    private String comment;
//    private Integer synchronized_from;
    private Long synchronized_from;

    /**
     * Default constructor
     */
    public Subject() {
    }


    /**
     * Constructor to create the object
     *
     *
     * @param study study the subject is part of
     * @param email email-adress of a subject (can be null for data protection)
     * @param subjectId string to identify a subject
     * @param comment remarks on a subject
     * @param synchronized_from
     */
    public Subject(Study study, String email, String subjectId, String comment, Long synchronized_from) {
        this.study=study;
        this.email = email;
        this.subjectId = subjectId;
        this.comment = comment;
        this.synchronized_from = synchronized_from;
    }

    public Long getId() {
        return id;
    }

    public Study getStudy() {
        study.getName();
        return study;
    }


    public String getEmail() {
        return email;
    }


    public String getSubjectId() {
        return subjectId;
    }


    public String getComment() {
        return comment;
    }


    public Long getSynchronized_from() {
        return synchronized_from;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return id.equals(subject.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudy(Study study) {
        this.study=study;
    }
}
