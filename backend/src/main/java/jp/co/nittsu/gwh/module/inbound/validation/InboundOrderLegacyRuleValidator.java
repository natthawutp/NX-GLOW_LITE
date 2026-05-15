package jp.co.nittsu.gwh.module.inbound.validation;

import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterRequest;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRegistrationRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class InboundOrderLegacyRuleValidator {

    private static final Pattern PIK_FALLBACK_PATTERN = Pattern.compile("^[A-Za-z0-9_\\-./: ]*$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9]+$");
    private static final BigDecimal MAX_WEIGHT = new BigDecimal("999999.999999");
    private static final BigDecimal MAX_VOLUME = new BigDecimal("999999.999999");

    public void validateOrThrow(
            InboundOrderRegisterRequest request,
            String companyCode,
            String warehouseCode,
            String customerCode,
            InboundOrderRegistrationRepository repository
    ) {
        List<InboundOrderValidationError> errors = new ArrayList<>();

        if (request == null || request.getHeader() == null) {
            errors.add(new InboundOrderValidationError("ERR0001", "Header is required", null));
            throw new InboundOrderValidationException(errors);
        }

        if (request.getHeader().getScheduledDate() == null) {
            errors.add(new InboundOrderValidationError("ERR0001", "Scheduled date is required", null));
        }

        String transactionKind = trimToNull(request.getHeader().getTransactionKind());
        InboundOrderRegistrationRepository.TransactionRule transactionRule =
                repository.getTransactionRule(companyCode, warehouseCode, customerCode, transactionKind);
        if (transactionKind != null && transactionRule != null
                && isYes(transactionRule.getReasonRequiredFlag())
                && trimToNull(request.getHeader().getRemarks()) == null) {
            errors.add(new InboundOrderValidationError("ERR0001", "Reason/remarks is required for this transaction kind", null));
        }

        String supplierCode = trimToNull(request.getHeader().getSupplierCode());
        if (supplierCode == null) {
            errors.add(new InboundOrderValidationError("ERR0001", "Supplier code is required", null));
        } else if (!repository.existsSupplierCode(companyCode, warehouseCode, customerCode, supplierCode)) {
            errors.add(new InboundOrderValidationError("ERR0004", "Supplier code does not exist", null));
        }

        String referenceNumber = trimToNull(request.getHeader().getReferenceNumber());
        if (referenceNumber != null && repository.existsReferenceNumber(companyCode, warehouseCode, customerCode, referenceNumber)) {
            errors.add(new InboundOrderValidationError("ERR0006", "Reference number already exists", null));
        }

        String poNumber = trimToNull(request.getHeader().getPoNumber());
        if (poNumber != null && repository.existsPurchaseOrderNumber(companyCode, warehouseCode, customerCode, poNumber)) {
            errors.add(new InboundOrderValidationError("ERR0006", "PO number already exists", null));
        }

        List<InboundOrderRegisterRequest.Line> lines = request.getLines();
        List<InboundOrderRegistrationRepository.PikSetting> pikSettings =
                repository.getPikSettings(companyCode, warehouseCode, customerCode);
        Map<String, String> dateFormats = repository.getDateFormatByCode();
        if (lines == null || lines.isEmpty()) {
            errors.add(new InboundOrderValidationError("ERR0245", "At least one detail line is required", null));
        } else {
            validateLines(companyCode, warehouseCode, customerCode, repository, lines, transactionRule, pikSettings, dateFormats, errors);
        }

        if (!errors.isEmpty()) {
            throw new InboundOrderValidationException(errors);
        }
    }

    private void validateLines(
            String companyCode,
            String warehouseCode,
            String customerCode,
            InboundOrderRegistrationRepository repository,
            List<InboundOrderRegisterRequest.Line> lines,
            InboundOrderRegistrationRepository.TransactionRule transactionRule,
            List<InboundOrderRegistrationRepository.PikSetting> pikSettings,
            Map<String, String> dateFormats,
            List<InboundOrderValidationError> errors
    ) {
        boolean foundBlankProductLine = false;
        BigDecimal aggregateWeight = BigDecimal.ZERO;
        BigDecimal aggregateVolume = BigDecimal.ZERO;
        for (int index = 0; index < lines.size(); index++) {
            int rowIndex = index + 1;
            InboundOrderRegisterRequest.Line line = lines.get(index);
            if (line == null) {
                errors.add(new InboundOrderValidationError("ERR0245", "Detail line is empty", rowIndex));
                continue;
            }

            String productCode = trimToNull(line.getProductCode());
            if (productCode == null) {
                foundBlankProductLine = true;
                continue;
            }
            if (foundBlankProductLine) {
                errors.add(new InboundOrderValidationError("ERR0245", "Product code appears after an empty detail line", rowIndex));
            }

            if (!repository.existsProductCode(companyCode, warehouseCode, customerCode, productCode)) {
                errors.add(new InboundOrderValidationError("ERR0004", "Product code does not exist: " + productCode, rowIndex));
            }

            String originCode = trimToNull(line.getOriginCode());
            if (originCode != null && !repository.existsOriginCode(companyCode, warehouseCode, customerCode, originCode)) {
                errors.add(new InboundOrderValidationError("ERR0700", "Origin code does not exist: " + originCode, rowIndex));
            }

            BigDecimal caseQty = normalizeQuantity(line.getPlannedCaseQuantity());
            BigDecimal pieceQty = normalizeQuantity(line.getPlannedPieceQuantity());
            if (caseQty.compareTo(BigDecimal.ZERO) <= 0 && pieceQty.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(new InboundOrderValidationError("ERR0251", "Case qty or piece qty must be greater than zero", rowIndex));
            }

            BigDecimal lineWeight = normalizeQuantity(line.getWeight());
            BigDecimal lineVolume = normalizeQuantity(line.getVolumeM3());
            if (lineWeight.compareTo(MAX_WEIGHT) > 0) {
                errors.add(new InboundOrderValidationError("ERR1036", "Line weight exceeds maximum", rowIndex));
            }
            if (lineVolume.compareTo(MAX_VOLUME) > 0) {
                errors.add(new InboundOrderValidationError("ERR1037", "Line volume exceeds maximum", rowIndex));
            }
            aggregateWeight = aggregateWeight.add(lineWeight);
            aggregateVolume = aggregateVolume.add(lineVolume);

            validatePikLine(line, rowIndex, transactionRule, pikSettings, dateFormats, errors);
        }

        if (aggregateWeight.compareTo(MAX_WEIGHT) > 0) {
            errors.add(new InboundOrderValidationError("ERR1038", "Total weight exceeds maximum", null));
        }
        if (aggregateVolume.compareTo(MAX_VOLUME) > 0) {
            errors.add(new InboundOrderValidationError("ERR1003", "Total volume exceeds maximum", null));
        }
    }

    private void validatePikLine(
            InboundOrderRegisterRequest.Line line,
            int rowIndex,
            InboundOrderRegistrationRepository.TransactionRule transactionRule,
            List<InboundOrderRegistrationRepository.PikSetting> pikSettings,
            Map<String, String> dateFormats,
            List<InboundOrderValidationError> errors
    ) {
        Map<Integer, String> pikValues = new HashMap<>();
        String[] raw = {
                line.getPik1(), line.getPik2(), line.getPik3(),
                line.getPik4(), line.getPik5(), line.getPik6(), line.getPik7()
        };
        for (int i = 0; i < raw.length; i++) {
            pikValues.put(i + 1, trimToNull(raw[i]));
        }

        if (pikSettings == null || pikSettings.isEmpty()) {
            for (int index = 1; index <= raw.length; index++) {
                String value = pikValues.get(index);
                if (value == null) {
                    continue;
                }
                if (value.length() > 40 || !PIK_FALLBACK_PATTERN.matcher(value).matches()) {
                    errors.add(new InboundOrderValidationError("ERR1004", "PIK" + index + " is invalid", rowIndex));
                }
            }
            return;
        }

        boolean requireScanWhenPkdpOff = transactionRule != null && "N".equalsIgnoreCase(defaultText(transactionRule.getPickDependencyFlag(), ""));
        for (InboundOrderRegistrationRepository.PikSetting setting : pikSettings) {
            int pikNumber = setting.getPikNumber();
            if (pikNumber < 1 || pikNumber > 7 || !isYes(setting.getUseFlag())) {
                continue;
            }
            String value = pikValues.get(pikNumber);

            boolean requiredByMaster = isYes(setting.getInputRequiredFlag());
            boolean requiredByScan = requireScanWhenPkdpOff && isScan(setting.getScanKind());
            if ((requiredByMaster || requiredByScan) && value == null) {
                errors.add(new InboundOrderValidationError("ERR1004", "PIK" + pikNumber + " is required", rowIndex));
                continue;
            }
            if (value == null) {
                continue;
            }

            if (!validatePikType(setting, value, dateFormats, rowIndex, errors)) {
                continue;
            }
            if (!PIK_FALLBACK_PATTERN.matcher(value).matches()) {
                errors.add(new InboundOrderValidationError("ERR1004", "PIK" + pikNumber + " contains invalid characters", rowIndex));
            }
        }
    }

    private boolean validatePikType(
            InboundOrderRegistrationRepository.PikSetting setting,
            String value,
            Map<String, String> dateFormats,
            int rowIndex,
            List<InboundOrderValidationError> errors
    ) {
        String type = defaultText(setting.getTypeKind(), "");

        if ("01".equals(type)) {
            String formatCode = trimToNull(setting.getInputFormatKind());
            String dateFormat = formatCode == null ? null : dateFormats.get(formatCode);
            if (!isValidDate(value, dateFormat)) {
                errors.add(new InboundOrderValidationError("ERR0040", "PIK" + setting.getPikNumber() + " date format is invalid", rowIndex));
                return false;
            }
        }

        if ("02".equals(type) && !NUMERIC_PATTERN.matcher(value).matches()) {
            errors.add(new InboundOrderValidationError("ERR1005", "PIK" + setting.getPikNumber() + " must be numeric", rowIndex));
            return false;
        }

        int maxLength = setting.getMaxLength() == null || setting.getMaxLength() <= 0
                ? 30
                : setting.getMaxLength();
        if (("02".equals(type) || "03".equals(type)) && value.length() > maxLength) {
            errors.add(new InboundOrderValidationError("ERR0039", "PIK" + setting.getPikNumber() + " length exceeds " + maxLength, rowIndex));
            return false;
        }

        return true;
    }

    private boolean isValidDate(String value, String pattern) {
        if (value == null) {
            return false;
        }
        String normalized = convertLegacyDatePattern(pattern);
        if (normalized == null) {
            normalized = "yyyyMMdd";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(normalized);
            LocalDate.parse(value, formatter);
            return true;
        } catch (IllegalArgumentException | DateTimeParseException ex) {
            return false;
        }
    }

    private String convertLegacyDatePattern(String pattern) {
        String source = trimToNull(pattern);
        if (source == null) {
            return null;
        }
        String normalized = source;
        normalized = normalized.replace("YYYY", "yyyy");
        normalized = normalized.replace("YY", "yy");
        normalized = normalized.replace("DD", "dd");
        return normalized;
    }

    private boolean isYes(String value) {
        return "Y".equalsIgnoreCase(defaultText(value, "")) || "1".equals(defaultText(value, ""));
    }

    private boolean isScan(String value) {
        return "1".equals(defaultText(value, ""));
    }

    private BigDecimal normalizeQuantity(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String defaultText(String value, String defaultValue) {
        String normalized = trimToNull(value);
        return normalized == null ? defaultValue : normalized;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}