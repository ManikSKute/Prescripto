package prescripto.backend.dto;

import lombok.Data;

@Data
public class BookAppointmentRequest {
    private Long docId;
    private String slotDate;
    private String slotTime;
}