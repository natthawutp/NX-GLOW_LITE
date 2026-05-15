package jp.co.nittsu.gwh.module.lpn.service;

import jp.co.nittsu.gwh.module.lpn.dto.*;
import jp.co.nittsu.gwh.module.lpn.repository.LpnRepository;
import jp.co.nittsu.gwh.module.lpn.repository.LpnRepository.LpnSearchResult;
import jp.co.nittsu.gwh.module.lpn.validation.LpnValidator;
import jp.co.nittsu.gwh.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LpnService {

    @Autowired
    private LpnRepository lpnRepository;

    @Autowired
    private LpnValidator lpnValidator;

    @Autowired
    private TenantContext tenantContext;

    public boolean isBarcodeAlreadyUsed(String barcode) {
        return lpnRepository.existsByLpnNumOrRfNum(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode(),
            barcode
        );
    }

    public LpnSearchResult search(String lpnNumber, String lpnRfNumber, String status, String lpnType,
                                   String locationCode, String arrivalNumber,
                                   int page, int size) {
        return lpnRepository.search(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode(),
            lpnNumber, lpnRfNumber, status, lpnType, locationCode, arrivalNumber,
            page, size
        );
    }

    @Transactional
    public void removeLpn(String lpnNumber, String user) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        LpnDto lpn = lpnRepository.getLpnDetail(cpny, whs, cust, lpnNumber);
        if (lpn == null) {
            throw new IllegalArgumentException("LPN not found: " + lpnNumber);
        }
        if ("809".equals(lpn.getLpnStatus())) {
            throw new IllegalArgumentException("Cannot remove confirmed LPN: " + lpnNumber);
        }

        String arrivalNumber = lpn.getArrivalNumber();

        // 1. Reduce AVR_RTPC_QTY on AV_R rows linked to this LPN's detail lines,
        //    then delete any rows where qty dropped to 0
        lpnRepository.reduceArrivalResultQtyByLpn(cpny, whs, cust, lpnNumber, user);
        lpnRepository.deleteZeroQtyArrivalResultByLpn(cpny, whs, cust, lpnNumber);

        // 2. Recheck arrival status
        if (arrivalNumber != null && !arrivalNumber.isEmpty()) {
            boolean hasAvR = lpnRepository.existsArrivalResultForArrival(cpny, whs, cust, arrivalNumber);
            String newStatus = hasAvR ? "205" : "100";
            lpnRepository.updateArrivalHeaderStatus(cpny, whs, cust, arrivalNumber, newStatus, user);
            lpnRepository.updateArrivalDetailStatus(cpny, whs, cust, arrivalNumber, newStatus, user);
        }

        // 3. Delete LPN detail and header
        lpnRepository.deleteLpnDetail(cpny, whs, cust, lpnNumber);
        lpnRepository.deleteLpnHeader(cpny, whs, cust, lpnNumber);
    }

    public LpnDto getDetail(String lpnNumber) {
        return lpnRepository.getLpnDetail(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode(),
            lpnNumber
        );
    }

    @Transactional
    public LpnDto create(LpnCreateRequest request, String user) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        List<String> errors = lpnValidator.validateCreateRequest(request, cpny, whs, cust);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        String lpnNumber = request.getLpnNumber();
        if (lpnNumber == null || lpnNumber.isEmpty()) {
            lpnNumber = lpnRepository.generateLpnNumber(whs);
        }

        int totalQty = 0;
        if (request.getContents() != null) {
            for (LpnCreateRequest.ContentLine line : request.getContents()) {
                totalQty += (line.getQuantity() != null ? line.getQuantity() : 0);
            }
        }

        lpnRepository.insertLpnHeader(cpny, whs, cust, lpnNumber,
            null,
            request.getLpnType(), "100", request.getParentLpnNumber(),
            request.getLocationCode(), request.getArrivalNumber(),
            totalQty, null, null, request.getBarcodeFormat(), request.getRemarks(), user);

        if (request.getContents() != null) {
            for (int i = 0; i < request.getContents().size(); i++) {
                LpnCreateRequest.ContentLine line = request.getContents().get(i);
                lpnRepository.insertLpnContent(cpny, whs, cust, lpnNumber, i + 1,
                    line.getProductCode(), line.getLotNumber(), line.getQuantity(),
                    line.getSubInventoryCode(), line.getArrivalNumber(), line.getArrivalLineNumber(),
                    line.getArrivalSeqNumber(),
                    line.getPik1(), line.getPik2(), line.getPik3(), line.getPik4(),
                    line.getPik5(), line.getPik6(), line.getPik7(), user);
            }
        }

        return lpnRepository.getLpnDetail(cpny, whs, cust, lpnNumber);
    }

    @Transactional
    public LpnDto receive(LpnReceiveRequest request, String user) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        List<String> errors = lpnValidator.validateReceiveRequest(request, cpny, whs, cust);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        // Always auto-generate LPN_NUM; scanned barcode goes to LPN_RF_NUM
        String lpnNumber = lpnRepository.generateLpnNumber(whs);
        String lpnRfNumber = (request.getLpnRfNumber() != null && !request.getLpnRfNumber().trim().isEmpty())
                ? request.getLpnRfNumber().trim() : null;

        int totalQty = 0;
        for (LpnReceiveRequest.ReceiveLine item : request.getItems()) {
            totalQty += (item.getQuantity() != null ? item.getQuantity() : 0);
        }

        lpnRepository.insertLpnHeader(cpny, whs, cust, lpnNumber,
            lpnRfNumber,
            request.getLpnType(), "200", null, "RECEIVING",
            request.getArrivalNumber(), totalQty, null, null,
            "CUSTOM", null, user);

        // LPN receive only creates LPN header + content rows.
        // Stock records are created by the normal inbound receive flow.
        // LPN content lines reference stock via arrival number/line/seq.
        // Skip insert if arrival line/seq numbers are 0 or null (invalid).
        int contentLine = 1;
        for (LpnReceiveRequest.ReceiveLine item : request.getItems()) {
            Integer avlnNum = item.getArrivalLineNumber();
            Integer avseqNum = item.getArrivalSeqNumber();
            if (avlnNum == null || avlnNum == 0 || avseqNum == null || avseqNum == 0) {
                continue;
            }
            lpnRepository.insertLpnContent(cpny, whs, cust, lpnNumber, contentLine++,
                item.getProductCode(), item.getLotNumber(), item.getQuantity(),
                item.getSubInventoryCode(), request.getArrivalNumber(), avlnNum,
                avseqNum,
                item.getPik1(), item.getPik2(), item.getPik3(), item.getPik4(),
                item.getPik5(), item.getPik6(), item.getPik7(), user);
        }

        // Update LPN_TTL_QTY = COUNT of successfully inserted detail lines
        lpnRepository.updateLpnTotalQtyFromDetail(cpny, whs, cust, lpnNumber, user);

        return lpnRepository.getLpnDetail(cpny, whs, cust, lpnNumber);
    }

    @Transactional
    public LpnDto putaway(LpnPutawayRequest request, String user) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        List<String> errors = lpnValidator.validatePutawayRequest(request, cpny, whs, cust);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        // Update LPN header location
        lpnRepository.updateLpnLocation(cpny, whs, cust,
            request.getLpnNumber(), request.getTargetLocationCode(), "400", user);

        // Update stock location for all stock records referenced by this LPN's content lines
        lpnRepository.updateStockLocationByLpn(cpny, whs, cust,
            request.getLpnNumber(),
            request.getTargetAreaCode(),
            request.getTargetRackCode(),
            request.getTargetPositionCode(),
            request.getTargetLevelCode(),
            user);

        return lpnRepository.getLpnDetail(cpny, whs, cust, request.getLpnNumber());
    }

    @Transactional
    public void updateStatus(String lpnNumber, String newStatus, String user) {
        lpnRepository.updateLpnStatus(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode(),
            lpnNumber, newStatus, user
        );
    }
}
