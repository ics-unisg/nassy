package com.dcap.fileReader;

/**
 * Class from Cheetah_Web1.0
 * some modification by uli
 *
 * The type DataFilException.
 * This exception is thrown when there went something wrong with a datafile,
 * for example if the columns don't have the same length
 */
public class DataFileException extends Exception {
    /**
     * Instantiates a new Data file exception.
     *
     * @param message the message
     */
    public DataFileException(String message) {
        super(message);
    }
}
