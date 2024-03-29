package com.itdev.exceptions;

public class OutOfQuantityException extends RuntimeException{

    public OutOfQuantityException(String message) {
        super(message);
    }
}
