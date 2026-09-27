package prescripto.backend.dto;

import lombok.Data;

@Data
public class DoctorLoginRequest {
    private String email;
    private String password;
}