package org.ylab.exceptions;

public class EmailFormatException extends RuntimeException{
    public EmailFormatException(String email) {
        super("This does not looks like email: " + email);
    }
}
