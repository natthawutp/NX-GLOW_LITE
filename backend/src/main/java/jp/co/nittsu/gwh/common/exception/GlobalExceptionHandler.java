package jp.co.nittsu.gwh.common.exception;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler for all REST controllers.
 * Maps business exceptions to appropriate HTTP responses with error codes
 * compatible with existing WMS error code system (ERR0174, ERR0216, etc.).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WmsBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(WmsBusinessException ex) {
        logger.warn("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        ApiResponse<Void> response = ApiResponse.error(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(WmsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(WmsNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.warning("ERR0174", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WmsOptimisticLockException.class)
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLock(WmsOptimisticLockException ex) {
        ApiResponse<Void> response = ApiResponse.error("ERR0285", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus("ERROR");
        ex.getBindingResult().getFieldErrors().forEach(error ->
            response.addMessage("ERR0216", error.getField() + ": " + error.getDefaultMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Invalid request: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.error("ERR0216", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        ApiResponse<Void> response = ApiResponse.error("ERR4214", "Access denied");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        logger.error("Unhandled exception", ex);
        ApiResponse<Void> response = ApiResponse.error("ERR9999", "An internal error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
