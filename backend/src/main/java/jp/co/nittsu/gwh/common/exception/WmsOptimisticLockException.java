package jp.co.nittsu.gwh.common.exception;

public class WmsOptimisticLockException extends RuntimeException {
    public WmsOptimisticLockException(String message) {
        super(message);
    }
}
