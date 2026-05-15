package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDetailDto;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDto;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRepository;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRepository.InboundOrderSearchResult;
import jp.co.nittsu.gwh.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InboundOrderService {

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private TenantContext tenantContext;

    @PersistenceContext
    private EntityManager em;

    public InboundOrderSearchResult search(
            String arrivalNo,
            String status,
            String supplier,
            LocalDate dateFrom,
            LocalDate dateTo,
            int page,
            int size
    ) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        InboundOrderSearchResult result = inboundOrderRepository.search(
                cpny,
                whs,
                cust,
                arrivalNo,
                status,
                supplier,
                dateFrom,
                dateTo,
                page,
                size
        );

        Map<String, String> statusLabelMap = loadInboundStatusLabels(cpny, whs, cust);
        List<InboundOrderDto> records = result.getRecords();
        for (InboundOrderDto row : records) {
            String statusCode = row.getArrivalStatus();
            row.setArrivalStatusLabel(statusLabelMap.getOrDefault(statusCode, mapStatusLabel(statusCode)));
        }
        return result;
    }

    /**
     * Get full arrival order detail (header + lines) for detail/edit screens.
     */
    public InboundOrderDetailDto getDetail(String arrivalNumber) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        InboundOrderDetailDto detail = inboundOrderRepository.findDetailByArrivalNumber(cpny, whs, cust, arrivalNumber);
        if (detail == null) {
            return null;
        }

        List<InboundOrderDetailDto.Line> lines = inboundOrderRepository.findDetailLines(cpny, whs, cust, arrivalNumber);
        detail.setLines(lines);

        Map<String, String> statusLabelMap = loadInboundStatusLabels(cpny, whs, cust);
        detail.setArrivalStatusLabel(
                statusLabelMap.getOrDefault(detail.getArrivalStatus(), mapStatusLabel(detail.getArrivalStatus())));
        for (InboundOrderDetailDto.Line line : lines) {
            line.setLineStatusLabel(
                    statusLabelMap.getOrDefault(line.getLineStatus(), mapStatusLabel(line.getLineStatus())));
        }

        // Set editable flag based on status
        detail.setEditable(InboundOrderUpdateService.isEditableStatus(detail.getArrivalStatus()));

        // Load arrival results (inspection/receiving data)
        List<InboundOrderDetailDto.ArrivalResult> results = inboundOrderRepository.findArrivalResults(cpny, whs, cust, arrivalNumber);
        for (InboundOrderDetailDto.ArrivalResult r : results) {
            r.setResultStatusLabel(
                    statusLabelMap.getOrDefault(r.getResultStatus(), mapStatusLabel(r.getResultStatus())));
        }
        detail.setArrivalResults(results);

        // Load LPN data linked to this arrival
        List<InboundOrderDetailDto.LpnInfo> lpns = inboundOrderRepository.findArrivalLpns(cpny, whs, cust, arrivalNumber);
        detail.setLpns(lpns);

        return detail;
    }

    private Map<String, String> loadInboundStatusLabels(String cpny, String whs, String cust) {
        Map<String, LabelCandidate> bestByCode = new HashMap<>();
        try {
            String sql = "SELECT STS_CPNY_COD, STS_WHS_COD, STS_CUST_COD, STS_COD, STS_BSNS_COD, STS_LBL_COD, STS_RMKS " +
                    "FROM SGWH0001.GWH_TM_STS " +
                    "WHERE STS_CPNY_COD IN (:cpny, '000000000000', '*') " +
                    "AND STS_WHS_COD IN (:whs, '00000000', '*') " +
                    "AND STS_CUST_COD IN (:cust, '00000000000000000000000000', '*') " +
                    "AND DEL_FLG = '0'";

            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            for (Object[] row : rows) {
                String rowCpny = row[0] != null ? row[0].toString().trim() : "";
                String rowWhs = row[1] != null ? row[1].toString().trim() : "";
                String rowCust = row[2] != null ? row[2].toString().trim() : "";
                String code = row[3] != null ? row[3].toString().trim() : "";
                String bsns = row[4] != null ? row[4].toString().trim() : "";
                String lblCode = row[5] != null ? row[5].toString().trim() : "";
                String remarks = row[6] != null ? row[6].toString().trim() : "";

                if (code.isEmpty()) {
                    continue;
                }

                String display = !remarks.isEmpty() ? remarks : lblCode;
                if (display.isEmpty()) {
                    continue;
                }

                int score = inboundModuleScore(bsns) + specificityScore(cpny, whs, cust, rowCpny, rowWhs, rowCust);
                LabelCandidate current = bestByCode.get(code);
                if (current == null || score > current.score) {
                    bestByCode.put(code, new LabelCandidate(display, score));
                }
            }
        } catch (Exception ex) {
            // Fallback to hardcoded labels
        }

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, LabelCandidate> entry : bestByCode.entrySet()) {
            result.put(entry.getKey(), entry.getValue().label);
        }
        return result;
    }

    private int inboundModuleScore(String bsns) {
        if (bsns == null || bsns.isEmpty()) {
            return 1;
        }
        String upper = bsns.toUpperCase();
        if (upper.contains("AV") || upper.contains("IN")) {
            return 3;
        }
        return 2;
    }

    private int specificityScore(String cpny, String whs, String cust, String rowCpny, String rowWhs, String rowCust) {
        int score = 0;
        if (cpny != null && cpny.equals(rowCpny)) {
            score += 6;
        }
        if (whs != null && whs.equals(rowWhs)) {
            score += 4;
        }
        if (cust != null && cust.equals(rowCust)) {
            score += 2;
        }
        return score;
    }

    private static class LabelCandidate {
        private final String label;
        private final int score;

        private LabelCandidate(String label, int score) {
            this.label = label;
            this.score = score;
        }
    }

    private String mapStatusLabel(String status) {
        if (status == null) {
            return "";
        }
        switch (status.trim()) {
            case "100":
                return "Pre";
            case "200":
                return "Located";
            case "205":
                return "Inspecting";
            case "209":
                return "Inspected";
            case "300":
                return "Receiving";
            case "305":
                return "Locating";
            case "306":
                return "Located";
            case "500":
                return "Putaway";
            case "605":
                return "Putaway Done";
            case "609":
                return "Stored";
            case "700":
                return "Closed";
            case "809":
                return "Confirmed";
            case "999":
                return "Cancelled";
            case "1":
            case "PENDING":
                return "Pending";
            case "2":
            case "RECEIVING":
                return "Receiving";
            case "3":
            case "CONFIRMED":
                return "Confirmed";
            case "4":
            case "COMPLETED":
            case "PUTAWAY":
                return "Completed";
            case "9":
            case "CANCELLED":
                return "Cancelled";
            default:
                return status;
        }
    }
}
