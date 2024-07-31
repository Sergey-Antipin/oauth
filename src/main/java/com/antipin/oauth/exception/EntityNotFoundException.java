package com.antipin.oauth.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("Not found entity with id " + id);
    }

    public EntityNotFoundException(String name) {
        super("Not found entity with name: "+ name);
    }
}
