package com.java.api;

import com.java.api.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Void> handleMissingEntity(NotFoundException exception) {
        return ResponseEntity.status(NotFoundException.HTTP_STATUS).build();
    }
}
