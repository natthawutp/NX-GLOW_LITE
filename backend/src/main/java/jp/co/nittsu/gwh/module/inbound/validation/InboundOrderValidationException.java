package jp.co.nittsu.gwh.module.inbound.validation;

import java.util.Collections;
import java.util.List;

public class InboundOrderValidationException extends RuntimeException {

    private final List<InboundOrderValidationError> errors;

    public InboundOrderValidationException(List<InboundOrderValidationError> errors) {
        super("Inbound order validation failed");
        this.errors = errors == null ? Collections.<InboundOrderValidationError>emptyList() : errors;
    }

    public List<InboundOrderValidationError> getErrors() {
        return errors;
    }
}