package com.itdev.exceptions;

import java.io.Serializable;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String entityType, Serializable id) {
        super("The " + entityType + " with id = " + id + " does not exist");
    }
}
