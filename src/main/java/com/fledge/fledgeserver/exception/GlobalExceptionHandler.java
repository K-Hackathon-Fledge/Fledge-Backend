package com.fledge.fledgeserver.exception;

import static com.fledge.fledgeserver.exception.ErrorCode.INVALID_REQUEST;
import static com.fledge.fledgeserver.exception.ErrorCode.RESOURCE_NOT_FOUND;

import com.fledge.fledgeserver.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        String message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        return ErrorResponse.toResponseEntity(INVALID_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintValidException(ConstraintViolationException e) {
        return ErrorResponse.toResponseEntity(INVALID_REQUEST, e.getMessage().substring(19));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException is occurred. ", e);
        return ErrorResponse.toResponseEntity(RESOURCE_NOT_FOUND, RESOURCE_NOT_FOUND.getMessage());
    }

}
