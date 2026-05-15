package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.config.GwhTransactionProperties;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionLineDto;
import jp.co.nittsu.gwh.module.inbound.dto.ReceiveArrivalDto;
import jp.co.nittsu.gwh.module.inbound.dto.ScanItemRequest;
import jp.co.nittsu.gwh.module.inbound.dto.ScanItemResponse;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalInspectionRepository;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalInspectionRepository.TransactionPermitFlags;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for InboundReceiveService covering the complete receiving flow:
 * AVH header update, AVD detail update, AVR result insert/accumulate,
 * and Transaction Log (GWH_TJ_XT) insert.
 */
@ExtendWith(MockitoExtension.class)
class InboundReceiveServiceTest {

    private static final String CPNY = "520010";
    private static final String WHS = "SP";
    private static final String CUST = "CUST01";
    private static final String USER = "TESTUSER";
    private static final String AV_NUM = "AV20260320001";

    @Mock private InboundOrderRepository orderRepo;
    @Mock private ArrivalInspectionRepository inspRepo;
    @Mock private TenantContext tenantContext;
    @Mock private GwhTransactionProperties transProps;

    @InjectMocks
    private InboundReceiveService service;

    private GwhTransactionProperties.Receiving receivingConfig;
    private GwhTransactionProperties.ReceivingLimits limits;
    private GwhTransactionProperties.HhtInspectionMode hhtMode;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContext.getCompanyCode()).thenReturn(CPNY);
        lenient().when(tenantContext.getWarehouseCode()).thenReturn(WHS);
        lenient().when(tenantContext.getCustomerCode()).thenReturn(CUST);
        lenient().when(tenantContext.getUserId()).thenReturn(USER);

        receivingConfig = new GwhTransactionProperties.Receiving();
        hhtMode = new GwhTransactionProperties.HhtInspectionMode();
        hhtMode.setEnabled(false);
        receivingConfig.setHhtInspectionMode(hhtMode);
        limits = new GwhTransactionProperties.ReceivingLimits();
        receivingConfig.setLimits(limits);
        receivingConfig.setValidArrivalTransactionKinds(new ArrayList<>());
        lenient().when(transProps.getReceiving()).thenReturn(receivingConfig);
    }

    // -----------------------------------------------------------------------
    // Helper builders
    // -----------------------------------------------------------------------

    private InboundOrderDto buildHeader(String status) {
        InboundOrderDto h = new InboundOrderDto();
        h.setArrivalNumber(AV_NUM);
        h.setArrivalStatus(status);
        h.setTransactionKind("001");
        h.setSupplierCode("SUP001");
        return h;
    }

    private InspectionLineDto buildLine(int lineNum, String prodCode, int planQty) {
        InspectionLineDto l = new InspectionLineDto();
        l.setLineNumber(lineNum);
        l.setProductCode(prodCode);
        l.setProductName("Product " + prodCode);
        l.setPlanCsQty(0);
        l.setPlanPcQty(planQty);
        l.setPlanBlQty(0);
        l.setPlanTotalQty(planQty);
        l.setPiecesPerCase(1);
        l.setPiecesPerBulk(1);
        l.setOriginCode("JP");
        l.setTrnKnd("001");
        l.setSubInventoryCode("-");
        l.setDmgFlg("N");
        l.setPik1(null); l.setPik2(null); l.setPik3(null); l.setPik4(null);
        l.setPik5(null); l.setPik6(null); l.setPik7(null);
        l.setAreaCode("A01"); l.setRackCode("R01"); l.setLevelCode("L01"); l.setPositionCode("P01");
        return l;
    }

    private ScanItemRequest buildScanRequest(String productCode, int qty) {
        ScanItemRequest req = new ScanItemRequest();
        req.setArrivalNumber(AV_NUM);
        req.setProductCode(productCode);
        req.setQuantity(qty);
        return req;
    }

    private void stubCommonMocks(String headerStatus, List<InspectionLineDto> lines) {
        // All stubs lenient — this helper sets up the happy-path; not every stub
        // is reached in every test (e.g. validation tests throw before later stubs).

        // PRCC
        lenient().when(inspRepo.findProcessControlStatuses(eq(CPNY), eq(WHS), eq(CUST), eq("apinsReg")))
                .thenReturn(new String[]{"200", null, "809"});

        // Lines
        lenient().when(inspRepo.getInspectionLines(eq(CPNY), eq(WHS), eq(CUST), eq(AV_NUM)))
                .thenReturn(lines);

        // Location resolve
        lenient().when(inspRepo.resolveRecommendedLocation(eq(CPNY), eq(WHS), eq(CUST), anyString()))
                .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

        // Location existence check (CHECK #12 — only reached when locationAutoResolved=false)
        lenient().when(inspRepo.locationExists(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString())).thenReturn(true);

        // Product weight/volume
        lenient().when(inspRepo.getProductWeightVolume(eq(CPNY), eq(WHS), eq(CUST), anyString()))
                .thenReturn(new double[]{0.5, 0.001});

        // Transaction permit flags
        lenient().when(inspRepo.getTransactionPermitFlags(eq(CPNY), eq(WHS), eq(CUST), eq(AV_NUM)))
                .thenReturn(new TransactionPermitFlags(true, true));

        // SKU / temp checks default to pass
        lenient().when(inspRepo.hasDifferentProductAtLocation(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(false);
        lenient().when(inspRepo.getProductTempControl(anyString(), anyString(), anyString(), anyString())).thenReturn("");
        lenient().when(inspRepo.locationMatchesTemperature(anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        // Header reload for response
        InboundOrderDto refreshed = buildHeader(headerStatus);
        lenient().when(orderRepo.findByArrivalNumber(eq(CPNY), eq(WHS), eq(CUST), eq(AV_NUM)))
                .thenReturn(refreshed);
    }

    // =======================================================================
    // loadArrival tests
    // =======================================================================

    @Nested
    @DisplayName("loadArrival")
    class LoadArrivalTests {

        @Test
        @DisplayName("returns arrival DTO when header found and status valid")
        void loadArrival_success() {
            InboundOrderDto header = buildHeader("200");
            when(orderRepo.findByArrivalNumber(CPNY, WHS, CUST, AV_NUM)).thenReturn(header);
            when(inspRepo.findProcessControlStatuses(CPNY, WHS, CUST, "apinsReg"))
                    .thenReturn(new String[]{"200", null, "809"});
            when(inspRepo.getInspectionLines(CPNY, WHS, CUST, AV_NUM))
                    .thenReturn(Collections.singletonList(buildLine(1, "PROD001", 10)));

            ReceiveArrivalDto result = service.loadArrival(AV_NUM);

            assertNotNull(result);
            assertEquals(AV_NUM, result.getArrivalNumber());
            assertEquals("200", result.getArrivalStatus());
            assertEquals(1, result.getLines().size());
            assertEquals(10, result.getTotalPlannedQty());
        }

        @Test
        @DisplayName("throws when arrival not found")
        void loadArrival_notFound() {
            when(orderRepo.findByArrivalNumber(CPNY, WHS, CUST, AV_NUM)).thenReturn(null);

            assertThrows(IllegalArgumentException.class, () -> service.loadArrival(AV_NUM));
        }

        @Test
        @DisplayName("throws ReceiveViolationException when status below PRCC required")
        void loadArrival_statusTooLow() {
            InboundOrderDto header = buildHeader("100");
            when(orderRepo.findByArrivalNumber(CPNY, WHS, CUST, AV_NUM)).thenReturn(header);
            when(inspRepo.findProcessControlStatuses(CPNY, WHS, CUST, "apinsReg"))
                    .thenReturn(new String[]{"200", null, "809"});
            when(transProps.getErrorMessage("ERR1027")).thenReturn("Status not ready");

            assertThrows(ReceiveViolationException.class, () -> service.loadArrival(AV_NUM));
        }

        @Test
        @DisplayName("throws ReceiveViolationException when already completed")
        void loadArrival_alreadyCompleted() {
            InboundOrderDto header = buildHeader("809");
            when(orderRepo.findByArrivalNumber(CPNY, WHS, CUST, AV_NUM)).thenReturn(header);
            when(inspRepo.findProcessControlStatuses(CPNY, WHS, CUST, "apinsReg"))
                    .thenReturn(new String[]{"200", null, "809"});

            assertThrows(ReceiveViolationException.class, () -> service.loadArrival(AV_NUM));
        }
    }

    // =======================================================================
    // processItemScan tests — AVR insert/accumulate
    // =======================================================================

    @Nested
    @DisplayName("processItemScan — AVR operations")
    class ProcessItemScanAvrTests {

        @Test
        @DisplayName("first scan: inserts new AVR record")
        void firstScan_insertsAvr() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(1);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(1);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));

            verify(inspRepo).insertArrivalResult(
                    eq(CPNY), eq(WHS), eq(CUST), eq(AV_NUM), eq(1), eq(1),
                    eq("PROD001"), eq("JP"), eq("001"), anyString(),
                    eq(1), eq(1), eq(0), eq(1), eq(0), eq(1),
                    eq("-"),
                    isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                    eq("A01"), eq("R01"), eq("L01"), eq("P01"),
                    eq("N"), anyString(),
                    anyDouble(), anyDouble(),
                    isNull(), eq(USER));
            verify(inspRepo, never()).accumulateArrivalResult(any(), any(), any(), any(), anyInt(), anyInt(), any(), any());

            assertEquals("PROD001", res.getProductCode());
            assertEquals(1, res.getResultTotalQty());
        }

        @Test
        @DisplayName("second scan: accumulates onto existing AVR")
        void secondScan_accumulatesAvr() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(5);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(5);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));

            verify(inspRepo).accumulateArrivalResult(CPNY, WHS, CUST, AV_NUM, 1, 1, null, USER);
            verify(inspRepo, never()).insertArrivalResult(any(), any(), any(), any(), anyInt(), anyInt(),
                    any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), anyInt(), anyInt(), any(),
                    any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any());

            assertEquals(5, res.getResultTotalQty());
            assertEquals("SHORT", res.getReceiveStatus());
        }
    }


    // =======================================================================
    // processItemScan tests — Transaction Log (GWH_TJ_XT) operations
    // =======================================================================

    @Nested
    @DisplayName("processItemScan — Transaction Log (XT) operations")
    class ProcessItemScanXtTests {

        @Test
        @DisplayName("inserts XT record on every scan")
        void scan_insertsXt() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(1);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(1);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            service.processItemScan(buildScanRequest("PROD001", 1));

            // XT should be called with asKnd="AV" and opKnd = first 2 chars of avspSts
            verify(inspRepo).insertTransactionLog(
                    eq(CPNY), eq(WHS), eq(CUST),
                    eq(AV_NUM), eq(1), eq(1),
                    eq("AV"), eq("20"),  // "200" -> "20"
                    eq(USER));
        }

        @Test
        @DisplayName("XT failure does not block receiving — scan succeeds")
        void xtFailure_doesNotBlock() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(1);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(1);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);
            doThrow(new RuntimeException("ORA-00001: unique constraint violated"))
                    .when(inspRepo).insertTransactionLog(any(), any(), any(), any(), anyInt(), anyInt(), any(), any(), any());

            // Should not throw
            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));
            assertEquals("PROD001", res.getProductCode());
        }
    }

    // =======================================================================
    // processItemScan tests — AVD + AVH status progression
    // =======================================================================

    @Nested
    @DisplayName("processItemScan — Status progression")
    class ProcessItemScanStatusTests {

        @Test
        @DisplayName("line exact match → line status 209, header stays 300 if other lines incomplete")
        void lineExactMatch_headerStillReceiving() {
            InspectionLineDto line1 = buildLine(1, "PROD001", 5);
            InspectionLineDto line2 = buildLine(2, "PROD002", 10);
            List<InspectionLineDto> lines = Arrays.asList(line1, line2);
            stubCommonMocks("300", lines);

            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(5); // equals plan
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(5);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));

            // Line status should be 209 (inspected exact)
            verify(inspRepo).updateDetailStatus(CPNY, WHS, CUST, AV_NUM, 1, "209", USER);
            verify(inspRepo).updateResultStatus(CPNY, WHS, CUST, AV_NUM, 1, "209", USER);
            // Header stays 300 because not all lines are complete
            verify(inspRepo).updateHeaderStatus(CPNY, WHS, CUST, AV_NUM, "300", USER);
            assertEquals("OK", res.getReceiveStatus());
        }

        @Test
        @DisplayName("all lines complete exact → header promoted to 209")
        void allLinesComplete_headerPromoted209() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 5));
            stubCommonMocks("209", lines);

            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(5);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(5);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(true);

            // All lines have exact match (no anyShort, no anyDiscrepancy)
            InspectionLineDto completeLine = buildLine(1, "PROD001", 5);
            completeLine.setResultTotalQty(5);
            when(inspRepo.getInspectionLines(CPNY, WHS, CUST, AV_NUM))
                    .thenReturn(Collections.singletonList(completeLine));

            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));

            verify(inspRepo).updateHeaderStatus(CPNY, WHS, CUST, AV_NUM, "209", USER);
            assertEquals("209", res.getHeaderStatus());
        }

        @Test
        @DisplayName("over-receive with surplus allowed → line status 205")
        void overReceive_surplusAllowed() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 5));
            stubCommonMocks("300", lines);

            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(7); // > 5
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(7);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));

            verify(inspRepo).updateDetailStatus(CPNY, WHS, CUST, AV_NUM, 1, "205", USER);
            assertEquals("OVER", res.getReceiveStatus());
            assertNotNull(res.getReceiveWarning());
        }

        @Test
        @DisplayName("over-receive with surplus NOT allowed → throws ERR1033")
        void overReceive_surplusNotAllowed() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 5));
            stubCommonMocks("300", lines);

            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(6);
            // getTotalReceivedQty not stubbed — over-receive check throws before it's reached
            // Surplus NOT allowed
            when(inspRepo.getTransactionPermitFlags(CPNY, WHS, CUST, AV_NUM))
                    .thenReturn(new TransactionPermitFlags(false, false));

            assertThrows(ReceiveViolationException.class,
                    () -> service.processItemScan(buildScanRequest("PROD001", 1)));
        }
    }

    // =======================================================================
    // processItemScan tests — Validation checks
    // =======================================================================

    @Nested
    @DisplayName("processItemScan — Validation")
    class ProcessItemScanValidationTests {

        @Test
        @DisplayName("throws when product not found on arrival")
        void productNotFound() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);

            assertThrows(IllegalArgumentException.class,
                    () -> service.processItemScan(buildScanRequest("UNKNOWN_PROD", 1)));
        }

        @Test
        @DisplayName("SKU mixing check throws ERR1346")
        void skuMixingCheck() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);
            when(inspRepo.hasDifferentProductAtLocation(eq(CPNY), eq(WHS), eq(CUST),
                    eq("A01"), eq("R01"), eq("L01"), eq("P01"), eq("PROD001")))
                    .thenReturn(true);
            when(transProps.getErrorMessage("ERR1346")).thenReturn("SKU mixing not allowed");

            assertThrows(ReceiveViolationException.class,
                    () -> service.processItemScan(buildScanRequest("PROD001", 1)));
        }

        @Test
        @DisplayName("temperature mismatch throws ERR1348")
        void temperatureMismatch() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);
            when(inspRepo.hasDifferentProductAtLocation(any(), any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(false);
            when(inspRepo.getProductTempControl(CPNY, WHS, CUST, "PROD001")).thenReturn("COLD");
            when(inspRepo.locationMatchesTemperature(eq(CPNY), eq(WHS),
                    eq("A01"), eq("R01"), eq("L01"), eq("P01"), eq("COLD")))
                    .thenReturn(false);
            when(transProps.getErrorMessage("ERR1348")).thenReturn("Temperature mismatch");

            assertThrows(ReceiveViolationException.class,
                    () -> service.processItemScan(buildScanRequest("PROD001", 1)));
        }
    }

    // =======================================================================
    // processItemScan tests — Complete end-to-end scenario
    // =======================================================================

    @Nested
    @DisplayName("processItemScan — End-to-end scenario")
    class ProcessItemScanE2ETests {

        @Test
        @DisplayName("full cycle: 3 scans to complete a single-line arrival")
        void fullCycle_singleLine() {
            InspectionLineDto line = buildLine(1, "PROD001", 3);
            List<InspectionLineDto> lines = Collections.singletonList(line);

            // --- Scan 1: first item ---
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(1);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(1);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemResponse res1 = service.processItemScan(buildScanRequest("PROD001", 1));
            assertEquals(1, res1.getResultTotalQty());
            assertEquals("SHORT", res1.getReceiveStatus());
            assertEquals("300", res1.getHeaderStatus());

            // Verify all operations for scan 1
            verify(inspRepo).insertArrivalResult(any(), any(), any(), any(), anyInt(), anyInt(),
                    any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), anyInt(), anyInt(), any(),
                    any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any());
            verify(inspRepo).insertTransactionLog(any(), any(), any(), any(), anyInt(), anyInt(), any(), any(), any());

            // --- Scan 2: second item ---
            reset(inspRepo);
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(2);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(2);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemResponse res2 = service.processItemScan(buildScanRequest("PROD001", 1));
            assertEquals(2, res2.getResultTotalQty());
            assertEquals("SHORT", res2.getReceiveStatus());
            verify(inspRepo).accumulateArrivalResult(CPNY, WHS, CUST, AV_NUM, 1, 1, null, USER);

            // --- Scan 3: final item, line completes, all lines complete ---
            reset(inspRepo);
            InspectionLineDto completeLine = buildLine(1, "PROD001", 3);
            completeLine.setResultTotalQty(3);
            List<InspectionLineDto> completedLines = Collections.singletonList(completeLine);
            stubCommonMocks("209", completedLines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(3);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(3);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(true);

            ScanItemResponse res3 = service.processItemScan(buildScanRequest("PROD001", 1));
            assertEquals(3, res3.getResultTotalQty());
            assertEquals("OK", res3.getReceiveStatus());
            assertEquals("209", res3.getHeaderStatus());

            // Verify final status updates
            verify(inspRepo).updateDetailStatus(CPNY, WHS, CUST, AV_NUM, 1, "209", USER);
            verify(inspRepo).updateHeaderStatus(CPNY, WHS, CUST, AV_NUM, "209", USER);
        }

        @Test
        @DisplayName("multi-line arrival: header stays 300 until all lines complete")
        void multiLine_headerWaitsForAllLines() {
            InspectionLineDto line1 = buildLine(1, "PROD001", 2);
            InspectionLineDto line2 = buildLine(2, "PROD002", 3);
            List<InspectionLineDto> lines = Arrays.asList(line1, line2);

            // Scan line 1 to completion
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(true);
            when(inspRepo.getAvrLocation(CPNY, WHS, CUST, AV_NUM, 1))
                    .thenReturn(new String[]{"A01", "R01", "P01", "L01"});

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(2); // exact
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(2);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false); // line 2 still pending

            ScanItemResponse res = service.processItemScan(buildScanRequest("PROD001", 1));

            // Line 1 is done but header stays 300
            verify(inspRepo).updateDetailStatus(CPNY, WHS, CUST, AV_NUM, 1, "209", USER);
            verify(inspRepo).updateHeaderStatus(CPNY, WHS, CUST, AV_NUM, "300", USER);
            assertEquals("300", res.getHeaderStatus());
        }
    }

    // =======================================================================
    // processItemScan tests — LPN handling
    // =======================================================================

    @Nested
    @DisplayName("processItemScan — LPN")
    class ProcessItemScanLpnTests {

        @Test
        @DisplayName("LPN number passed through to AVR insert and response")
        void lpnPassedToAvrAndResponse() {
            List<InspectionLineDto> lines = Collections.singletonList(buildLine(1, "PROD001", 10));
            stubCommonMocks("300", lines);
            when(inspRepo.avrExists(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(false);

            when(inspRepo.getLineReceivedQty(CPNY, WHS, CUST, AV_NUM, 1)).thenReturn(1);
            when(inspRepo.getTotalReceivedQty(CPNY, WHS, CUST, AV_NUM)).thenReturn(1);
            when(inspRepo.allDetailLinesComplete(CPNY, WHS, CUST, AV_NUM)).thenReturn(false);

            ScanItemRequest req = buildScanRequest("PROD001", 1);
            req.setLpnNumber("LPN-001");
            req.setLpnType("PLT");

            ScanItemResponse res = service.processItemScan(req);

            assertEquals("LPN-001", res.getLpnNumber());
            // Verify LPN passed to insertArrivalResult (34th parameter)
            verify(inspRepo).insertArrivalResult(
                    any(), any(), any(), any(), anyInt(), anyInt(),
                    any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), anyInt(), anyInt(), any(),
                    any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                    any(), any(),
                    eq("LPN-001"), any());
        }
    }
}
