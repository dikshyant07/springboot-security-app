package com.authentication.system.security_project.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleExceptions(Exception e) {
        HttpStatus httpStatus;
        if (e instanceof UserAlreadyExistsException) httpStatus = HttpStatus.CONFLICT;
        else if (e instanceof ExpiredJwtException) httpStatus = HttpStatus.UNAUTHORIZED;
        else httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, e.getMessage());
        problemDetail.setProperty("Exception Time", LocalDateTime.now());
        problemDetail.setProperty("Status", "Failed");
        return new ResponseEntity<>(problemDetail, httpStatus);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, null);
        problemDetail.setProperty("Exception Time", LocalDateTime.now());
        problemDetail.setProperty("Status", "Failed");
        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        problemDetail.setProperty("Errors", errorMap);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);

    }
}
