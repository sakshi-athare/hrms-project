package com.hrms.common.exception;


public class InvalidLeaveStateException extends RuntimeException {

    public InvalidLeaveStateException(String message) {
        super(message);
    }
}