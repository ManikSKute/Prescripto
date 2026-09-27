package prescripto.backend.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String phone;
    private String dob;
    private String gender;
    private String addressLine1;
    private String addressLine2;
}