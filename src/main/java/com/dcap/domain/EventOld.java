package com.dcap.domain;

import javax.persistence.*;

/**
 * This class describes events that are logged during an examination
 * There are instantinous events and temporal events.
 * An event is instantinous if it does not have a start time
 *
 * @author uli
 */

@Entity
@Table(name = "events")
public class EventOld implements DatabaseEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_event")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_subject")
    private Subject subject;

    @Column(name = "time_start")
    private Long startTime;
    @Column(name = "time_end")
    private Long endTime;
    private String type;
    private String name;
    @Column(columnDefinition = "json")
    private String attributes;


    /**
     * Standard constructor
     */
    public EventOld() {

    }


    /**
     * Constructor fo an event
     * @param startTime start time of an event (if this is null, then the event is instantinous)
     * @param endTime end time of an event
     * @param type describes the type of an event
     * @param name name of the EventOld
     * @param attributes attributes connected with an event, stored in JSON
     * @param subject subject that is the agent of the event
     */
    public EventOld(Long startTime, Long endTime, String type, String name, String attributes, Subject subject) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.name = name;
        this.attributes = attributes;
        this.subject=subject;
    }


   /**
    * The id as in the database
    *
   * @return the id
    */
    public Long getId() {
        return id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventOld eventOld = (EventOld) o;

        return id.equals(eventOld.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
