package com.Nirlvy.Newbackend.exception;

import com.Nirlvy.Newbackend.common.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Result> handle(ServiceException serviceException) {
        Result result = new Result();
        result.setCode(serviceException.getCode());
        result.setMsg(serviceException.getMessage());
        return ResponseEntity.status(serviceException.getCode()).body(result);
    }
}