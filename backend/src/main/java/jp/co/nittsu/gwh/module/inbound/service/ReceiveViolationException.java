package jp.co.nittsu.gwh.module.inbound.service;

public class ReceiveViolationException extends RuntimeException {
    private final String errorCode;

    public ReceiveViolationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
