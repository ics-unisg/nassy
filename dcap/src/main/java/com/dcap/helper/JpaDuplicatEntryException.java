package com.dcap.helper;

/**
 * This exception is thrown whenever a database-entry is already in a database and and wasn't expected to be there
 */

public class JpaDuplicatEntryException extends Exception{

    public JpaDuplicatEntryException(){
    }

    /**
     * constructor
     *
     * @param message the message used to inform the user
     */
    public JpaDuplicatEntryException(String message){
        super(message);
    }
}
