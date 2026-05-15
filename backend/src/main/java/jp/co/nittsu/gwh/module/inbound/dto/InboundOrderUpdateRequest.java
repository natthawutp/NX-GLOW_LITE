package jp.co.nittsu.gwh.module.inbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Request DTO for updating an existing arrival order (PUT /inbound/orders/{arrivalNo}).
 * Reuses Header and Line inner classes from InboundOrderRegisterRequest.
 */
public class InboundOrderUpdateRequest {

    @NotNull
    @JsonProperty("AVH_AV_NUM")
    private String arrivalNumber;

    @NotNull
    @JsonProperty("UPD_YMDHMS")
    private String updTimestamp;

    @NotNull
    @Valid
    @JsonProperty("header")
    private InboundOrderRegisterRequest.Header header;

    @Valid
    @JsonProperty("lines")
    private List<InboundOrderRegisterRequest.Line> lines = new ArrayList<>();

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getUpdTimestamp() { return updTimestamp; }
    public void setUpdTimestamp(String updTimestamp) { this.updTimestamp = updTimestamp; }
    public InboundOrderRegisterRequest.Header getHeader() { return header; }
    public void setHeader(InboundOrderRegisterRequest.Header header) { this.header = header; }
    public List<InboundOrderRegisterRequest.Line> getLines() { return lines; }
    public void setLines(List<InboundOrderRegisterRequest.Line> lines) { this.lines = lines; }
}
