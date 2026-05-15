package jp.co.nittsu.gwh.common.exception;

/**
 * Business logic exception with WMS error code.
 * Maps to existing error codes: ERR0174 (not found), ERR0216 (required),
 * ERR1012 (read error), ERR1013 (insert error), ERR1014 (update error).
 */
public class WmsBusinessException extends RuntimeException {

    private final String errorCode;
    private final Object[] params;

    public WmsBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.params = null;
    }

    public WmsBusinessException(String errorCode, String message, Object[] params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public String getErrorCode() { return errorCode; }
    public Object[] getParams() { return params; }
}
