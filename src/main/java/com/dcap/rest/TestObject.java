package com.dcap.rest;

import java.util.Date;

public class TestObject {
    private String name;
    private String message;

    public TestObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
    }


}


