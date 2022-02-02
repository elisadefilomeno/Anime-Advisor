package it.unipi.large_scale.anime_advisor.exceptions;

public class DuplicateAnimeException extends Exception{
    public DuplicateAnimeException() {
        super();
    }

    public DuplicateAnimeException(String message) {
        super(message);
    }
}
