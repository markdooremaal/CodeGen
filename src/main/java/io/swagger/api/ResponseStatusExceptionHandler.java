package io.swagger.api;

import io.swagger.model.dto.ExceptionDTO;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//Class to handle all errors
@ControllerAdvice
public class ResponseStatusExceptionHandler extends ResponseEntityExceptionHandler {
    //Handle ResponseStatusExceptions only
    @Order(1)
    @ExceptionHandler(value = { ResponseStatusException.class })
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        ExceptionDTO dto = new ExceptionDTO(ex.getReason(), ex.getMessage());
        return handleExceptionInternal(ex, dto, new HttpHeaders(),
                HttpStatus.valueOf(ex.getStatus().value()), request);
    }

    //Handle the rest
    @Order(2)
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleResponseException(Exception ex, WebRequest request) {
        ExceptionDTO dto = new ExceptionDTO(ex.getMessage());
        return handleExceptionInternal(ex, dto, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
}
