package com.dcap.rest;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The DataMessage, that is send to the client.
 *
 * @param <T> the type parameter
 *
 * @author uli
 */
public class DataMsg<T>  {

    private final Integer statusCode;
    private final String messageMachineReadable;
    private final String messageHumanReadable;
    private final T resBody;

    /**
     * Instantiates a new Data msg.
     *
     * @param statusCode             the status code
     * @param messageMachineReadable the message machine readable
     * @param messageHumanReadable   the message human readable
     * @param resBody           the object to send
     */
    @JsonCreator
    public DataMsg(@JsonProperty("statusCode") Integer statusCode,
                   @JsonProperty("messageMachineReadable") String messageMachineReadable,
                   @JsonProperty("messageHumanReadable") String messageHumanReadable,
                   @JsonProperty("resBody") T resBody) {
        this.statusCode = statusCode;
        this.messageMachineReadable = messageMachineReadable;
        this.messageHumanReadable = messageHumanReadable;
        this.resBody = resBody;
    }

    /**
     * Gets status code.
     *  The code should be 0 if everything is fine, and other if there are problems
     * @return the status code
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * Gets message machine readable. If everything is fine, this should be null;
     *
     * @return the message machine readable
     */
    public String getMessageMachineReadable() {
        return messageMachineReadable;
    }

    /**
     * Gets message human readable, I everything is fine, this should be null
     *
     * @return the message human readable
     */
    public String getMessageHumanReadable() {
        return messageHumanReadable;
    }

    /**
     * Gets object to send. This is used for all kind of objects
     *
     * @return the object to send
     */
    public T getResBody() {
        return resBody;
    }
}
