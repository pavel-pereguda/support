package com.codexsoft.servicesupport.main.config.exception;

public class ServiceClientException extends RuntimeException {
    private String errorCode;
    private String customMessage;

    public ServiceClientException(String errorCode, String customMessage) {
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public ServiceClientException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if (errorCode != null) {
            return errorCode;
        } else if (customMessage != null) {
            return customMessage;
        }
        return super.getMessage();
    }
}
