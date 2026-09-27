package prescripto.backend.dto;

import lombok.Data;

@Data
public class AddDoctorRequest {
    private String name;
    private String email;
    private String password;
    private String speciality;
    private String degree;
    private String experience;
    private String about;
    private Double fees;
    private String addressLine1;
    private String addressLine2;
    private String image; // Base64 string or image URL
}