package com.dcap.service.Exceptions;

/**
 * Class  RepoExeption. This Exception is thrown if something was wrong in the repository operation
 */
public class RepoExeption extends Exception {

    /**
     * Instantiates a new RepoExeption.
     *
     * @param message the message with the detailed description
     */
    public RepoExeption(String message) {
        super(message);
    }
}
