package prescripto.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import prescripto.backend.dto.*;
import prescripto.backend.entity.Appointment;
import prescripto.backend.entity.Doctor;
import prescripto.backend.repository.AppointmentRepository;
import prescripto.backend.repository.DoctorRepository;
import prescripto.backend.repository.UserRepository;
import prescripto.backend.security.JwtUtils;

import java.util.*;

@Service
public class AdminService {

	@Value("${admin.email:admin@prescripto.com}")
	private String adminEmail;

	@Value("${admin.password:admin123}")
	private String adminPassword;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	// Admin Login
	public AuthResponse loginAdmin(AdminLoginRequest request) {
		if (adminEmail.equals(request.getEmail()) && adminPassword.equals(request.getPassword())) {
			String token = jwtUtils.generateToken(adminEmail, "ROLE_ADMIN");
			return new AuthResponse(true, token, "Admin Login successful");
		}
		return new AuthResponse(false, "Invalid credentials");
	}

	// Add New Doctor
	public AuthResponse addDoctor(AddDoctorRequest request, MultipartFile imageFile) {
		if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
			return new AuthResponse(false, "Missing Details");
		}

		Doctor doctor = new Doctor();
		doctor.setName(request.getName());
		doctor.setEmail(request.getEmail());
		doctor.setPassword(passwordEncoder.encode(request.getPassword()));
		doctor.setSpeciality(request.getSpeciality());
		doctor.setDegree(request.getDegree());
		doctor.setExperience(request.getExperience());
		doctor.setAbout(request.getAbout());
		doctor.setFees(request.getFees());
		doctor.setAddressLine1(request.getAddressLine1());
		doctor.setAddressLine2(request.getAddressLine2());

		// Upload image to Cloudinary and store URL in MySQL
		if (imageFile != null && !imageFile.isEmpty()) {
			String imageUrl = cloudinaryService.uploadImage(imageFile);
			doctor.setImage(imageUrl);
		}

		doctorRepository.save(doctor);
		return new AuthResponse(true, null, "Doctor Added");
	}

	// Get All Doctors for Admin
	public List<Doctor> getAllDoctors() {
		return doctorRepository.findAll();
	}

	// Get All Appointments for Admin
	public List<Appointment> getAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		Collections.reverse(appointments);
		return appointments;
	}

	// Cancel Appointment by Admin
	public AuthResponse cancelAppointmentByAdmin(Map<String, Long> payload) {
		Long appointmentId = payload.get("appointmentId");
		Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

		if (appointmentOpt.isEmpty()) {
			return new AuthResponse(false, "Appointment not found");
		}

		Appointment appointment = appointmentOpt.get();
		appointment.setCancelled(true);
		appointmentRepository.save(appointment);

		return new AuthResponse(true, null, "Appointment Cancelled");
	}

	// Get Admin Dashboard Data
	public Map<String, Object> getAdminDashboardData() {
		long doctorsCount = doctorRepository.count();
		long usersCount = userRepository.count();
		List<Appointment> appointments = appointmentRepository.findAll();

		List<Appointment> latestAppointments = new ArrayList<>(appointments);
		Collections.reverse(latestAppointments);
		if (latestAppointments.size() > 5) {
			latestAppointments = latestAppointments.subList(0, 5);
		}

		Map<String, Object> dashData = new HashMap<>();
		dashData.put("doctors", doctorsCount);
		dashData.put("patients", usersCount);
		dashData.put("appointments", appointments.size());
		dashData.put("latestAppointments", latestAppointments);

		return dashData;
	}

	// Delete Doctor by Admin
	@Transactional
	public AuthResponse deleteDoctor(Long docId) {
		Optional<Doctor> doctorOpt = doctorRepository.findById(docId);
		if (doctorOpt.isEmpty()) {
			return new AuthResponse(false, "Doctor not found");
		}

		// Delete associated appointments first to prevent foreign key issues
		List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(docId);
		if (!doctorAppointments.isEmpty()) {
			appointmentRepository.deleteAll(doctorAppointments);
		}

		// Delete the doctor record
		doctorRepository.deleteById(docId);

		return new AuthResponse(true, null, "Doctor Deleted Successfully");
	}
}