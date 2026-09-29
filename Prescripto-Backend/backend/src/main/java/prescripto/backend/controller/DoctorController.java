package prescripto.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prescripto.backend.dto.*;
import prescripto.backend.entity.Doctor;
import prescripto.backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired private DoctorService doctorService;
    @Autowired private AdminService adminService;

    private Long getDocId(HttpServletRequest request) {
        return Long.parseLong((String) request.getAttribute("authenticatedSubject"));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctorList() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("doctors", doctorService.getAllDoctorsList());
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> addDoctor(
            @RequestPart("docData") AddDoctorRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return ResponseEntity.ok(adminService.addDoctor(request, imageFile));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request) {
        Doctor doctor = doctorService.getDoctorProfile(getDocId(request));
        Map<String, Object> response = new HashMap<>();
        response.put("success", doctor != null);
        if (doctor != null) response.put("profileData", doctor);
        else response.put("message", "Doctor profile not found");
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AuthResponse> updateProfile(
            HttpServletRequest request,
            @RequestPart("docData") UpdateDoctorProfileRequest profileRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return ResponseEntity.ok(doctorService.updateDoctorProfile(getDocId(request), profileRequest, imageFile));
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> changeAvailability(@PathVariable("id") Long id) {
        return ResponseEntity.ok(doctorService.changeAvailability(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> deleteDoctor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.deleteDoctor(id));
    }
}
