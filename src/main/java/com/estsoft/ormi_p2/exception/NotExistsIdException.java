package com.estsoft.ormi_p2.exception;

public class NotExistsIdException extends RuntimeException {
    public NotExistsIdException(Long id) {
        super("Comment with ID " + id + " does not exist.");
    }
}