//package prescripto.backend.dto;
//
//import lombok.Data;
//
//@Data
//public class UpdateDoctorProfileRequest {
//    private Double fees;
//    private String addressLine1;
//    private String addressLine2;
//    private Boolean available;
//}

package prescripto.backend.dto;

import lombok.Data;

@Data
public class UpdateDoctorProfileRequest {
    private String name;
    private String speciality;
    private String degree;
    private String experience;
    private String about;
    private Double fees;
    private String addressLine1;
    private String addressLine2;
    private Boolean available;
}