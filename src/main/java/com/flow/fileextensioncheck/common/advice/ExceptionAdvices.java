package com.flow.fileextensioncheck.common.advice;

import com.flow.fileextensioncheck.common.exception.DuplicateFileExtensionException;
import com.flow.fileextensioncheck.common.exception.ExtensionCountExceededException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class ExceptionAdvices {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<String> constraintViolationException(ConstraintViolationException ex) {
        log.info("ConstraintViolationException : {}", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateFileExtensionException.class)
    protected ResponseEntity<String> duplicateFileExtensionException(DuplicateFileExtensionException ex) {
        log.info("DuplicateFileExtensionException : {}", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ExtensionCountExceededException.class)
    protected ResponseEntity<String> extensionCountExceededException(ExtensionCountExceededException ex) {
        log.info("ExtensionCountExceededException : {}", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }
}