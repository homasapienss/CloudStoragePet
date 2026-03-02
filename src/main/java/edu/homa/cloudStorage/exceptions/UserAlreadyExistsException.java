package edu.homa.cloudStorage.exceptions;

public class UserAlreadyExistsException extends ApplicationException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
