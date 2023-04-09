package com.Nirlvy.Newbackend.exception;

import com.Nirlvy.Newbackend.common.ResultCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final Integer code;

    public ServiceException(ResultCode code, Exception e) {
        super(code.getMsg() + (e != null ? ":" + e.getMessage() : ""));
        this.code = code.getCode();
    }
}
