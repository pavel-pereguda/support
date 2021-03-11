package com.codexsoft.servicesupport.main.config.exception;

public class ServiceServerException extends RuntimeException {

    public ServiceServerException(Throwable cause) {
        super(cause);
    }

    public ServiceServerException(String message) {
        super(message);
    }

    public ServiceServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
