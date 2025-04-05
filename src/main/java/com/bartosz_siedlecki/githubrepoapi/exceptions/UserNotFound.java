package com.bartosz_siedlecki.githubrepoapi.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}