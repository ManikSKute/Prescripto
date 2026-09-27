package prescripto.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import prescripto.backend.dto.*;
import prescripto.backend.entity.Appointment;
import prescripto.backend.entity.Doctor;
import prescripto.backend.service.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody AdminLoginRequest request) {
        AuthResponse response = adminService.loginAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/add-doctor", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> addDoctor(
            @RequestPart("docData") AddDoctorRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        AuthResponse response = adminService.addDoctor(request, imageFile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        List<Doctor> doctors = adminService.getAllDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointments")
    public ResponseEntity<Map<String, Object>> getAllAppointments() {
        List<Appointment> appointments = adminService.getAllAppointments();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("appointments", appointments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel-appointment")
    public ResponseEntity<AuthResponse> cancelAppointment(@RequestBody Map<String, Long> payload) {
        AuthResponse response = adminService.cancelAppointmentByAdmin(payload);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashData = adminService.getAdminDashboardData();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("dashData", dashData);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/delete-doctor")
    public ResponseEntity<AuthResponse> deleteDoctor(@RequestBody Map<String, Long> payload) {
        Long docId = payload.get("docId");
        AuthResponse response = adminService.deleteDoctor(docId);
        return ResponseEntity.ok(response);
    }
}