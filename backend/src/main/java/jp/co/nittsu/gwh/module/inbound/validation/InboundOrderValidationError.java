package jp.co.nittsu.gwh.module.inbound.validation;

public class InboundOrderValidationError {

    private final String code;
    private final String message;
    private final Integer rowIndex;

    public InboundOrderValidationError(String code, String message, Integer rowIndex) {
        this.code = code;
        this.message = message;
        this.rowIndex = rowIndex;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }
}