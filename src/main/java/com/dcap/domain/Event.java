package com.dcap.domain;


/**
 * Class to log the events in the database
 * Made to be compatible with old Cheetah
 *
 * @author uli
 */
public class Event {

    private String name;
    private String type;
    private Long startTimeStamp;
    private Long endTimeStamp;
    private Long duration;
    private String additionalInformations;
    private ENUMERATED_EVENT_TYPE eventType;

    public Event(String name, String type, Long endTimeStamp, String additionalInformations) {
        this.name = name;
        this.type = type;
        this.endTimeStamp = endTimeStamp;
        this.additionalInformations = additionalInformations;
        this.eventType=ENUMERATED_EVENT_TYPE.INSTANTANIOUS;
    }

    public Event(String name, String type, Long startTimeStamp, Long endTimeStamp, String additionalInformations) {
        this.name = name;
        this.type = type;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
        this.additionalInformations = additionalInformations;
        if(startTimeStamp==null || startTimeStamp==0l){
            this.eventType = ENUMERATED_EVENT_TYPE.INSTANTANIOUS;
            this.duration=0l;
        }else{
            this.eventType= ENUMERATED_EVENT_TYPE.PHASE;
            this.duration=endTimeStamp- startTimeStamp;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public String getAdditionalInformations() {
        return additionalInformations;
    }

    public void setAdditionalInformations(String additionalInformations) {
        this.additionalInformations = additionalInformations;
    }

    public ENUMERATED_EVENT_TYPE getEventType() {
        return eventType;
    }

    public void setEventType(ENUMERATED_EVENT_TYPE eventType) {
        this.eventType = eventType;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
