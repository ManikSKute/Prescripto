package prescripto.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import prescripto.backend.dto.*;
import prescripto.backend.entity.Appointment;
import prescripto.backend.entity.Doctor;
import prescripto.backend.service.DoctorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	private Long getDoctorIdFromRequest(HttpServletRequest request) {
		String subject = (String) request.getAttribute("authenticatedSubject");
		return subject != null ? Long.parseLong(subject) : null;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginDoctor(@RequestBody DoctorLoginRequest request) {
		AuthResponse response = doctorService.loginDoctor(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getDoctorList() {
		List<Doctor> doctors = doctorService.getAllDoctorsList();
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("doctors", doctors);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/change-availability")
	public ResponseEntity<AuthResponse> changeAvailability(@RequestBody ChangeAvailabilityRequest request) {
		AuthResponse response = doctorService.changeAvailability(request.getDocId());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/appointments")
	public ResponseEntity<Map<String, Object>> getDoctorAppointments(HttpServletRequest request) {
		Long doctorId = getDoctorIdFromRequest(request);
		List<Appointment> appointments = doctorService.getDoctorAppointments(doctorId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("appointments", appointments);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/complete-appointment")
	public ResponseEntity<AuthResponse> completeAppointment(HttpServletRequest request,
			@RequestBody Map<String, Long> payload) {
		Long doctorId = getDoctorIdFromRequest(request);
		AuthResponse response = doctorService.completeAppointment(doctorId, payload);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/cancel-appointment")
	public ResponseEntity<AuthResponse> cancelAppointment(HttpServletRequest request,
			@RequestBody Map<String, Long> payload) {
		Long doctorId = getDoctorIdFromRequest(request);
		AuthResponse response = doctorService.cancelAppointmentByDoctor(doctorId, payload);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/profile")
	public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request) {
		Long doctorId = getDoctorIdFromRequest(request);
		Doctor doctor = doctorService.getDoctorProfile(doctorId);

		Map<String, Object> response = new HashMap<>();
		if (doctor != null) {
			response.put("success", true);
			response.put("profileData", doctor);
		} else {
			response.put("success", false);
			response.put("message", "Doctor profile not found");
		}
		return ResponseEntity.ok(response);
	}

//    @PostMapping("/update-profile")
//    public ResponseEntity<AuthResponse> updateProfile(HttpServletRequest request,
//                                                       @RequestBody UpdateDoctorProfileRequest profileRequest) {
//        Long doctorId = getDoctorIdFromRequest(request);
//        AuthResponse response = doctorService.updateDoctorProfile(doctorId, profileRequest);
//        return ResponseEntity.ok(response);
//    }

	@PostMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AuthResponse> updateProfile(HttpServletRequest request,
			@RequestPart("docData") UpdateDoctorProfileRequest profileRequest,
			@RequestPart(value = "image", required = false) MultipartFile imageFile) {

		Long doctorId = getDoctorIdFromRequest(request);
		AuthResponse response = doctorService.updateDoctorProfile(doctorId, profileRequest, imageFile);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<Map<String, Object>> getDashboard(HttpServletRequest request) {
		Long doctorId = getDoctorIdFromRequest(request);
		Map<String, Object> dashData = doctorService.getDoctorDashboardData(doctorId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("dashData", dashData);
		return ResponseEntity.ok(response);
	}
}