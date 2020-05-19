package com.dcap.service.Exceptions;

public class NoSuchFilterException extends Exception {
    public NoSuchFilterException(String name) {
        super(name);
    }
}
