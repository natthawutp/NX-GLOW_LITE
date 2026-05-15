package jp.co.nittsu.gwh.module.lpn.validation;

import jp.co.nittsu.gwh.module.lpn.dto.LpnCreateRequest;
import jp.co.nittsu.gwh.module.lpn.dto.LpnPutawayRequest;
import jp.co.nittsu.gwh.module.lpn.dto.LpnReceiveRequest;
import jp.co.nittsu.gwh.module.lpn.repository.LpnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LpnValidator {

    @Autowired
    private LpnRepository lpnRepository;

    public List<String> validateCreateRequest(LpnCreateRequest request,
                                               String cpny, String whs, String cust) {
        List<String> errors = new ArrayList<>();

        if (request.getLpnNumber() != null && !request.getLpnNumber().isEmpty()) {
            if (lpnRepository.existsLpn(cpny, whs, cust, request.getLpnNumber())) {
                errors.add("LPN number '" + request.getLpnNumber() + "' already exists");
            }
        }

        if (request.getContents() == null || request.getContents().isEmpty()) {
            errors.add("At least one content line is required");
            return errors;
        }

        Set<String> productCodes = new HashSet<>();
        for (int i = 0; i < request.getContents().size(); i++) {
            LpnCreateRequest.ContentLine line = request.getContents().get(i);
            if (line.getProductCode() == null || line.getProductCode().isEmpty()) {
                errors.add("Row " + (i + 1) + ": Product code is required");
            } else {
                productCodes.add(line.getProductCode());
            }
            if (line.getQuantity() == null || line.getQuantity() <= 0) {
                errors.add("Row " + (i + 1) + ": Quantity must be greater than 0");
            }
        }

        return errors;
    }

    public List<String> validateReceiveRequest(LpnReceiveRequest request,
                                                String cpny, String whs, String cust) {
        List<String> errors = new ArrayList<>();

        if (request.getArrivalNumber() == null || request.getArrivalNumber().isEmpty()) {
            errors.add("Arrival number is required");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            errors.add("At least one item is required");
            return errors;
        }

        if (request.getLpnRfNumber() != null && !request.getLpnRfNumber().isEmpty()) {
            if (lpnRepository.existsByLpnNumOrRfNum(cpny, whs, cust, request.getLpnRfNumber())) {
                errors.add("LPN Number already scanned.");
            }
        }

        for (int i = 0; i < request.getItems().size(); i++) {
            LpnReceiveRequest.ReceiveLine line = request.getItems().get(i);
            if (line.getProductCode() == null || line.getProductCode().isEmpty()) {
                errors.add("Row " + (i + 1) + ": Product code is required");
            }
            if (line.getQuantity() == null || line.getQuantity() <= 0) {
                errors.add("Row " + (i + 1) + ": Quantity must be greater than 0");
            }
        }

        return errors;
    }

    public List<String> validatePutawayRequest(LpnPutawayRequest request,
                                                String cpny, String whs, String cust) {
        List<String> errors = new ArrayList<>();

        if (request.getLpnNumber() == null || request.getLpnNumber().isEmpty()) {
            errors.add("LPN number is required");
        } else if (!lpnRepository.existsLpn(cpny, whs, cust, request.getLpnNumber())) {
            errors.add("LPN '" + request.getLpnNumber() + "' not found");
        }

        if (request.getTargetLocationCode() == null || request.getTargetLocationCode().isEmpty()) {
            errors.add("Target location is required");
        }

        return errors;
    }
}
