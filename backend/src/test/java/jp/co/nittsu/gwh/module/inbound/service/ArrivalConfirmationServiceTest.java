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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        when(arrivalConfirmationRepository.findExistingArrivalNumbers(eq("C01"), eq("W01"), eq("CU01"), any()))
            .thenReturn(Collections.singletonList("A001"));
        when(arrivalConfirmationRepository.findProcessFinalStatus("C01", "W01", "CU01", "apcnfReg"))
            .thenReturn(null);
        when(arrivalConfirmationRepository.findQuantityMismatchArrivalNumbers(eq("C01"), eq("W01"), eq("CU01"), any()))
            .thenReturn(Collections.emptyList());
        when(arrivalConfirmationRepository.confirmArrivals(eq("C01"), eq("W01"), eq("CU01"), any(), eq(LocalDate.of(2026, 3, 17)), eq("209"), any(), eq("MODERN_APCNF")))
            .thenReturn(1);

        ArrivalConfirmationConfirmResponse response = service.confirm(request);

        assertEquals(2, response.getRequestedCount());
        assertEquals(1, response.getConfirmedCount());
        assertEquals("209", response.getAppliedStatus());
        assertEquals(Collections.singletonList("A002"), response.getNotFoundArrivalNumbers());
    }

    @Test
    void confirmThrowsWhenQuantityMismatchExists() {
        ArrivalConfirmationConfirmRequest request = new ArrivalConfirmationConfirmRequest();
        request.setArrivalNumbers(Collections.singletonList("A001"));

        when(arrivalConfirmationRepository.findExistingArrivalNumbers(eq("C01"), eq("W01"), eq("CU01"), any()))
            .thenReturn(Collections.singletonList("A001"));
        when(arrivalConfirmationRepository.findProcessFinalStatus("C01", "W01", "CU01", "apcnfReg"))
            .thenReturn("209");
        when(arrivalConfirmationRepository.findQuantityMismatchArrivalNumbers(eq("C01"), eq("W01"), eq("CU01"), any()))
            .thenReturn(Collections.singletonList("A001"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.confirm(request));
        assertEquals(true, ex.getMessage().contains("ERR0415"));
    }
}
