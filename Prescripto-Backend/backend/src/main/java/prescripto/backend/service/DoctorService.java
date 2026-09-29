package prescripto.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import prescripto.backend.dto.*;
import prescripto.backend.entity.Appointment;
import prescripto.backend.entity.Doctor;
import prescripto.backend.repository.AppointmentRepository;
import prescripto.backend.repository.DoctorRepository;
import prescripto.backend.security.JwtUtils;

import java.util.*;

@Service
public class DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private CloudinaryService cloudinaryService;

	// Doctor Login
	public AuthResponse loginDoctor(DoctorLoginRequest request) {
		Optional<Doctor> doctorOpt = doctorRepository.findByEmail(request.getEmail());
		if (doctorOpt.isEmpty()) {
			return new AuthResponse(false, "Invalid credentials");
		}

		Doctor doctor = doctorOpt.get();
		if (!passwordEncoder.matches(request.getPassword(), doctor.getPassword())) {
			return new AuthResponse(false, "Invalid credentials");
		}

		String token = jwtUtils.generateToken(String.valueOf(doctor.getId()), "ROLE_DOCTOR");
		return new AuthResponse(true, token, "Login successful");
	}

	// Get All Public Doctors List
	public List<Doctor> getAllDoctorsList() {
		return doctorRepository.findAll();
	}

	// Toggle Doctor Availability
	public AuthResponse changeAvailability(Long docId) {
		Optional<Doctor> doctorOpt = doctorRepository.findById(docId);
		if (doctorOpt.isEmpty()) {
			return new AuthResponse(false, "Doctor not found");
		}

		Doctor doctor = doctorOpt.get();
		doctor.setAvailable(!Boolean.TRUE.equals(doctor.getAvailable()));
		doctorRepository.save(doctor);

		return new AuthResponse(true, null, "Availability Changed");
	}

	// Get Appointments for Doctor Panel
	public List<Appointment> getDoctorAppointments(Long doctorId) {
		List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
		Collections.reverse(appointments);
		return appointments;
	}

	// Mark Appointment Completed
	public AuthResponse completeAppointment(Long doctorId, Map<String, Long> payload) {
		Long appointmentId = payload.get("appointmentId");
		Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

		if (appointmentOpt.isEmpty()) {
			return new AuthResponse(false, "Appointment not found");
		}

		Appointment appointment = appointmentOpt.get();
		if (!appointment.getDoctor().getId().equals(doctorId)) {
			return new AuthResponse(false, "Unauthorized Action");
		}

		appointment.setIsCompleted(true);
		appointmentRepository.save(appointment);

		return new AuthResponse(true, null, "Appointment Completed");
	}

	// Cancel Appointment by Doctor
	public AuthResponse cancelAppointmentByDoctor(Long doctorId, Map<String, Long> payload) {
		Long appointmentId = payload.get("appointmentId");
		Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

		if (appointmentOpt.isEmpty()) {
			return new AuthResponse(false, "Appointment not found");
		}

		Appointment appointment = appointmentOpt.get();
		if (!appointment.getDoctor().getId().equals(doctorId)) {
			return new AuthResponse(false, "Unauthorized Action");
		}

		appointment.setCancelled(true);
		appointmentRepository.save(appointment);

		return new AuthResponse(true, null, "Appointment Cancelled");
	}

	// Get Doctor Profile
	public Doctor getDoctorProfile(Long doctorId) {
		return doctorRepository.findById(doctorId).orElse(null);
	}

	// Update Doctor Profile
//    public AuthResponse updateDoctorProfile(Long doctorId, UpdateDoctorProfileRequest request) {
//        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
//        if (doctorOpt.isEmpty()) {
//            return new AuthResponse(false, "Doctor not found");
//        }
//
//        Doctor doctor = doctorOpt.get();
//        if (request.getFees() != null) doctor.setFees(request.getFees());
//        if (request.getAddressLine1() != null) doctor.setAddressLine1(request.getAddressLine1());
//        if (request.getAddressLine2() != null) doctor.setAddressLine2(request.getAddressLine2());
//        if (request.getAvailable() != null) doctor.setAvailable(request.getAvailable());
//
//        doctorRepository.save(doctor);
//        return new AuthResponse(true, null, "Profile Updated");
//    }

	public AuthResponse updateDoctorProfile(Long doctorId, UpdateDoctorProfileRequest request,
			MultipartFile imageFile) {
		Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
		if (doctorOpt.isEmpty()) {
			return new AuthResponse(false, "Doctor not found");
		}

		Doctor doctor = doctorOpt.get();

		if (request.getName() != null && !request.getName().trim().isEmpty()) {
			doctor.setName(request.getName());
		}
		if (request.getSpeciality() != null && !request.getSpeciality().trim().isEmpty()) {
			doctor.setSpeciality(request.getSpeciality());
		}
		if (request.getDegree() != null && !request.getDegree().trim().isEmpty()) {
			doctor.setDegree(request.getDegree());
		}
		if (request.getExperience() != null && !request.getExperience().trim().isEmpty()) {
			doctor.setExperience(request.getExperience());
		}
		if (request.getAbout() != null && !request.getAbout().trim().isEmpty()) {
			doctor.setAbout(request.getAbout());
		}
		if (request.getFees() != null) {
			doctor.setFees(request.getFees());
		}
		if (request.getAddressLine1() != null) {
			doctor.setAddressLine1(request.getAddressLine1());
		}
		if (request.getAddressLine2() != null) {
			doctor.setAddressLine2(request.getAddressLine2());
		}
		if (request.getAvailable() != null) {
			doctor.setAvailable(request.getAvailable());
		}

		// Upload image to Cloudinary if provided
		if (imageFile != null && !imageFile.isEmpty()) {
			String imageUrl = cloudinaryService.uploadImage(imageFile);
			doctor.setImage(imageUrl);
		}

		doctorRepository.save(doctor);
		return new AuthResponse(true, null, "Profile Updated");
	}

	// Get Doctor Dashboard Data
	public Map<String, Object> getDoctorDashboardData(Long doctorId) {
		List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);

		double totalEarnings = 0.0;
		Set<Long> uniquePatientIds = new HashSet<>();

		for (Appointment app : appointments) {
			if (Boolean.TRUE.equals(app.getIsCompleted()) || Boolean.TRUE.equals(app.getPayment())) {
				totalEarnings += (app.getAmount() != null ? app.getAmount() : 0.0);
			}
			if (app.getUser() != null) {
				uniquePatientIds.add(app.getUser().getId());
			}
		}

		List<Appointment> latestAppointments = new ArrayList<>(appointments);
		Collections.reverse(latestAppointments);
		if (latestAppointments.size() > 5) {
			latestAppointments = latestAppointments.subList(0, 5);
		}

		Map<String, Object> dashData = new HashMap<>();
		dashData.put("earnings", totalEarnings);
		dashData.put("appointments", appointments.size());
		dashData.put("patients", uniquePatientIds.size());
		dashData.put("latestAppointments", latestAppointments);

		return dashData;
	}
}