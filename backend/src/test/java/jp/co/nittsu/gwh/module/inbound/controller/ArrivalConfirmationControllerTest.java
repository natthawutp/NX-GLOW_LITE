package jp.co.nittsu.gwh.module.inbound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmResponse;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalConfirmationRepository.SearchResult;
import jp.co.nittsu.gwh.module.inbound.service.ArrivalConfirmationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArrivalConfirmationControllerTest {

    @Mock
    private ArrivalConfirmationService arrivalConfirmationService;

    @InjectMocks
    private ArrivalConfirmationController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void confirmReturnsWarningWhenSomeArrivalsMissing() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        ArrivalConfirmationConfirmResponse response = new ArrivalConfirmationConfirmResponse();
        response.setRequestedCount(2);
        response.setConfirmedCount(1);
        response.setAppliedStatus("209");
        response.setNotFoundArrivalNumbers(Collections.singletonList("A999"));
        when(arrivalConfirmationService.confirm(any())).thenReturn(response);

        String request = "{\"arrivalNumbers\":[\"A001\",\"A999\"]}";

        mockMvc.perform(post("/api/v1/inbound/apcnf/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("WARNING"))
            .andExpect(jsonPath("$.data.confirmedCount").value(1));
    }

    @Test
    void confirmReturnsBadRequestWhenRuleValidationFails() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(arrivalConfirmationService.confirm(any()))
            .thenThrow(new IllegalStateException("ERR0415: quantity mismatch"));

        String request = "{\"arrivalNumbers\":[\"A001\"]}";

        mockMvc.perform(post("/api/v1/inbound/apcnf/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.messages[0].code").value("APCNF_CONFIRM_INVALID"));
    }

    @Test
    void searchReturnsSuccessEnvelope() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(arrivalConfirmationService.search(any()))
            .thenReturn(new SearchResult(Collections.emptyList(), 0L));

        mockMvc.perform(post("/api/v1/inbound/apcnf/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.totalRecords").value(0));
    }
}
