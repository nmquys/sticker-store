package com.nmquys.springbootstore.exception;

import com.nmquys.springbootstore.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//trả về JSON có cấu trúc khi có lỗi
@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class) //chặn mọi loại lỗi
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest)
    {
        ErrorResponseDto errorResponseDto =
                new ErrorResponseDto(webRequest.getDescription(false),
                                                                 HttpStatus.INTERNAL_SERVER_ERROR,
                                                                 exception.getMessage(),
                                                                 LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception)
    {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        fieldErrorList.forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
            ConstraintViolationException exception)
    {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolationSet = exception.getConstraintViolations();
        constraintViolationSet.forEach( violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
