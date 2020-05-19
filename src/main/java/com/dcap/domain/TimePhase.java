package com.dcap.domain;

import javax.persistence.*;

/**
 *This class describes a timephase as stored in the database. A timephase is a period of time that has a starttime and an
 * endtime, a name, a type. It is attached to a subject, and can also have a parent-timephase.
 *
 * @author uli
 */


@Entity
@Table(name = "time_phases")
public class TimePhase implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="pk_time_phase")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fk_subject")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "fk_parent_time_phase")
    private TimePhase parent;
    private String type;
    private String name;
    @Column(name = "time_start")
    private Long start;
    @Column(name = "time_end")
    private Long end;

    @Column(columnDefinition = "json")
    private String attributes;

    /**
     * default constructor
     */
    public TimePhase() {
    }


    /**
     * Constructor
     *
     * @param parent timephase which is the parent of this, only important if there are hierarchies, can be null
     * @param type type of the timephase, depends on experiment
     * @param name name of the particular timephase
     * @param start starttime of the timephase
     * @param end endtime of the timephase
     * @param attributes attributes that are attached to a timephase in json format
     * @param subejct subject that this timephase is attached to
     */
    public TimePhase(TimePhase parent, String type, String name, Long start, Long end, String attributes, Subject subejct) {
        this.parent = parent;
        this.subject=subejct;
        this.type = type;
        this.name = name;
        this.start = start;
        this.end = end;
        this.attributes = attributes;

    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public TimePhase getParent() {
        return parent;
    }

    public void setParent(TimePhase parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimePhase timePhase = (TimePhase) o;

        return id.equals(timePhase.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
