package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmRequest;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmResponse;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationSearchRequest;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalConfirmationRepository;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalConfirmationRepository.SearchResult;
import jp.co.nittsu.gwh.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArrivalConfirmationServiceTest {

    @Mock
    private TenantContext tenantContext;

    @Mock
    private ArrivalConfirmationRepository arrivalConfirmationRepository;

    @InjectMocks
    private ArrivalConfirmationService service;

    @BeforeEach
    void setUp() {
        when(tenantContext.getCompanyCode()).thenReturn("C01");
        when(tenantContext.getWarehouseCode()).thenReturn("W01");
        when(tenantContext.getCustomerCode()).thenReturn("CU01");
    }

    @Test
    void searchDelegatesToRepository() {
        ArrivalConfirmationSearchRequest request = new ArrivalConfirmationSearchRequest();
        SearchResult expected = new SearchResult(Collections.emptyList(), 0L);

        when(arrivalConfirmationRepository.search("C01", "W01", "CU01", request)).thenReturn(expected);

        SearchResult actual = service.search(request);

        assertEquals(expected, actual);
        verify(arrivalConfirmationRepository).search("C01", "W01", "CU01", request);
    }

    @Test
    void confirmUsesDefaultStatusAndReturnsResponse() {
        ArrivalConfirmationConfirmRequest request = new ArrivalConfirmationConfirmRequest();
        request.setArrivalNumbers(Arrays.asList("A001", "A001", "A002"));
        request.setArrivalDate(LocalDate.of(2026, 3, 17));

        stubConfirmMasterData("Y", "20260317");
        when(arrivalConfirmationRepository.findExistingArrivalNumbers(eq("C01"), eq("W01"), eq("CU01"), any()))
            .thenReturn(Collections.singletonList("A001"));
        when(arrivalConfirmationRepository.hasDetailsBelowRequiredStatus("C01", "W01", "CU01", "A001", "200"))
            .thenReturn(false);
        when(arrivalConfirmationRepository.getArrivalData("C01", "W01", "CU01", "A001"))
            .thenReturn(Collections.singletonList(arrivalDataRow(BigDecimal.TEN, BigDecimal.TEN, "Y", "Y")));
        when(arrivalConfirmationRepository.getTransactionKindPartialFlag("C01", "W01", "CU01", "A001"))
            .thenReturn(null);
        when(arrivalConfirmationRepository.confirmArrivals(
            eq("C01"), eq("W01"), eq("CU01"),
            eq(Collections.singletonList("A001")),
            eq(LocalDate.of(2026, 3, 17)),
            eq("209"),
            any(),
            eq("MODERN_APCNF")))
            .thenReturn(1);

        ArrivalConfirmationConfirmResponse response = service.confirm(request);

        assertEquals(2, response.getRequestedCount());
        assertEquals(1, response.getConfirmedCount());
        assertEquals("209", response.getAppliedStatus());
        assertEquals(Collections.singletonList("A002"), response.getNotFoundArrivalNumbers());
    }

    @Test
    void confirmThrowsWhenArrivalDateExceedsOperationDateWithoutPermission() {
        ArrivalConfirmationConfirmRequest request = new ArrivalConfirmationConfirmRequest();
        request.setArrivalNumbers(Collections.singletonList("A001"));
        request.setArrivalDate(LocalDate.of(2026, 3, 18));

        stubConfirmMasterData("N", "20260317");
        when(arrivalConfirmationRepository.findExistingArrivalNumbers(eq("C01"), eq("W01"), eq("CU01"), any()))
            .thenReturn(Collections.singletonList("A001"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.confirm(request));
        assertTrue(ex.getMessage().contains("ERR0415"));
    }

    private void stubConfirmMasterData(String futureDatePermission, String operationDate) {
        when(arrivalConfirmationRepository.getCustomerMaster("C01", "W01", "CU01"))
            .thenReturn(new Object[]{"N", "0", "N", "0"});
        when(arrivalConfirmationRepository.getOperationDate("C01", "W01", "CU01"))
            .thenReturn(operationDate);
        when(arrivalConfirmationRepository.findProcessControlStatuses("C01", "W01", "CU01", "apcnfReg"))
            .thenReturn(new String[]{"200", "205", "209"});
        when(arrivalConfirmationRepository.getPikAutoFlag("C01", "W01", "CU01"))
            .thenReturn(null);
        when(arrivalConfirmationRepository.getFutureArrivalDatePermission("C01", "W01", "CU01"))
            .thenReturn(futureDatePermission);
    }

    private Object[] arrivalDataRow(BigDecimal detailQty, BigDecimal totalQty, String sppaFlag, String shpaFlag) {
        Object[] row = new Object[30];
        row[0] = detailQty;
        row[1] = totalQty;
        row[2] = Timestamp.valueOf("2026-03-17 09:30:00");
        row[3] = "N";
        row[4] = sppaFlag;
        row[5] = shpaFlag;
        row[6] = "AS001";
        row[7] = 1;
        row[8] = 1;
        row[9] = "SKU001";
        row[10] = "ORG01";
        row[11] = "SB01";
        row[12] = "P1";
        row[13] = "P2";
        row[14] = "P3";
        row[15] = "P4";
        row[16] = "P5";
        row[17] = "P6";
        row[18] = "P7";
        row[19] = "AREA01";
        row[20] = "RACK01";
        row[21] = "POS01";
        row[22] = "LVL01";
        row[23] = totalQty;
        row[24] = BigDecimal.ZERO;
        row[25] = BigDecimal.ZERO;
        row[26] = "N";
        row[27] = "PCS";
        row[28] = "CS";
        row[29] = "N";
        return row;
    }
}
