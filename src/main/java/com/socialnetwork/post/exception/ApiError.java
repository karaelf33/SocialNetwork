package com.socialnetwork.post.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String message;
    private Throwable cause;

    public ApiError(HttpStatus status, String message, Throwable cause) {
        this.status = status;
        this.message = message;
        this.cause = cause;
    }

    // constructors, getters, and setters

}