package com.example.SWP391_FALL25.ExceptionHandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("message",ex.getMessage());
        map.put("status","Not found");
        map.put("path",request.getDescription(false));
        return new  ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException e){
        Map<String,Object> map = new HashMap<>();
        map.put("message",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        map.put("status",HttpStatus.BAD_REQUEST.value());
        map.put("error","Validation Errors");


        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request){
        Map<String,Object> map=new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("error", "Internal Server Error");
        map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        map.put("path", request.getDescription(false));
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
