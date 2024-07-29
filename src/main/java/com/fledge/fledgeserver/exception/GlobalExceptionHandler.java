package com.fledge.fledgeserver.exception;

import static com.fledge.fledgeserver.exception.ErrorCode.INVALID_REQUEST;
import static com.fledge.fledgeserver.exception.ErrorCode.DATA_INTEGRITY_VIOLATION;

import com.fledge.fledgeserver.exception.dto.ErrorResponse;

import com.fledge.fledgeserver.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        String message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        return ErrorResponse.toResponseEntity(INVALID_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintValidException(ConstraintViolationException e) {
        return ErrorResponse.toResponseEntity(INVALID_REQUEST, e.getMessage().substring(19));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException is occurred. ", e);
        return ErrorResponse.toResponseEntity(DATA_INTEGRITY_VIOLATION, DATA_INTEGRITY_VIOLATION.getMessage());
    }
}