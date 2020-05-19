package com.dcap.domain;

/**
 * Interface for objects out of a database. For the equal method, the id is needed.
 *
 * @author uli
 */

public interface DatabaseEntryEntity {
    /**
     * returns the id of the class, needed for equals method
     *
     * @return the id of the object
     */
    public Number getId();
}
