package jp.co.nittsu.gwh.module.inbound.dto;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class ArrivalConfirmationConfirmRequest {

    @NotEmpty
    private List<String> arrivalNumbers;
    private LocalDate arrivalDate;
    private Timestamp updTimestamp;

    public List<String> getArrivalNumbers() {
        return arrivalNumbers;
    }

    public void setArrivalNumbers(List<String> arrivalNumbers) {
        this.arrivalNumbers = arrivalNumbers;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Timestamp getUpdTimestamp() {
        return updTimestamp;
    }

    public void setUpdTimestamp(Timestamp updTimestamp) {
        this.updTimestamp = updTimestamp;
    }
}
