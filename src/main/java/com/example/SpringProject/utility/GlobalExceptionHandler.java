package com.example.SpringProject.utility;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.BusinessException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.ICinemaException;
import com.example.SpringProject.Exception.PaymentException;
import com.example.SpringProject.Exception.ResourceNotFoundException;


import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log LOGGER = LogFactory.getLog(GlobalExceptionHandler.class);

    @Autowired
    private Environment environment;

    // Handle ResourceNotFoundException (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.NOT_FOUND.value());
        apiError.setErrorMessage(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Handle BadRequestException (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.BAD_REQUEST.value());
        apiError.setErrorMessage(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle BusinessException (400)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.BAD_REQUEST.value());
        apiError.setErrorMessage(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle ConflictException (409)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(ConflictException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.CONFLICT.value());
        apiError.setErrorMessage(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // Handle PaymentException (402)
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ApiError> handlePaymentException(PaymentException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.PAYMENT_REQUIRED.value());
        apiError.setErrorMessage(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.PAYMENT_REQUIRED);
    }

    // Handle ICinemaException (Base - 400)
    @ExceptionHandler(ICinemaException.class)
    public ResponseEntity<ApiError> handleICinemaException(ICinemaException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.BAD_REQUEST.value());
        apiError.setErrorMessage(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle Validation Exceptions
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleValidationException(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        String errorMsg;
        
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manvException = (MethodArgumentNotValidException) exception;
            errorMsg = manvException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        } else {
            ConstraintViolationException cvException = (ConstraintViolationException) exception;
            errorMsg = cvException.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        }
        
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.BAD_REQUEST.value());
        apiError.setErrorMessage(errorMsg);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle General Exception (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        ApiError apiError = new ApiError();
        apiError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE"));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
