package dk.EppTecMockApp.controller;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;


public class CustomError {

    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public CustomError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public CustomError(HttpStatus status, String message, String error) {
        this(status, message, Arrays.asList(error));
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }
}
