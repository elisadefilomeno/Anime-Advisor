package it.unipi.large_scale.anime_advisor.exceptions;

public class DuplicateUserException extends Exception{
    public DuplicateUserException() {
        super();
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
