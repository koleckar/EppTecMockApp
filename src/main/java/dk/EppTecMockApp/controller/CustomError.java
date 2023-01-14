package dk.EppTecMockApp.controller;

import org.springframework.http.HttpStatus;

import java.util.List;

public record CustomError(HttpStatus status, String message, List<String> errors) {
    public CustomError(HttpStatus status, String message, String errors) {
        this(status, message, List.of(errors));
    }
}