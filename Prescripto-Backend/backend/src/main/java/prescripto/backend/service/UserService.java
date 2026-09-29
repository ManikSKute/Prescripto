package prescripto.backend.service;

import prescripto.backend.dto.*;
import prescripto.backend.entity.Appointment;
import prescripto.backend.entity.Doctor;
import prescripto.backend.entity.User;
import prescripto.backend.repository.AppointmentRepository;
import prescripto.backend.repository.DoctorRepository;
import prescripto.backend.repository.UserRepository;
import prescripto.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

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

	// Register User
	public AuthResponse registerUser(RegisterRequest request) {
		if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
			return new AuthResponse(false, "Missing Details");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			return new AuthResponse(false, "User with this email already exists");
		}
		if (request.getPassword().length() < 8) {
			return new AuthResponse(false, "Please enter a strong password (minimum 8 characters)");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		User savedUser = userRepository.save(user);
		String token = jwtUtils.generateToken(String.valueOf(savedUser.getId()), "ROLE_USER");

		return new AuthResponse(true, token, "User registered successfully");
	}

	// Login User
	public AuthResponse loginUser(LoginRequest request) {
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if (userOptional.isEmpty()) {
			return new AuthResponse(false, "User does not exist");
		}

		User user = userOptional.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return new AuthResponse(false, "Invalid credentials");
		}

		String token = jwtUtils.generateToken(String.valueOf(user.getId()), "ROLE_USER");
		return new AuthResponse(true, token, "Login successful");
	}

	// Get User Profile
	public User getUserProfile(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	// Update User Profile
//	public AuthResponse updateUserProfile(Long userId, UpdateProfileRequest request) {
//		Optional<User> userOptional = userRepository.findById(userId);
//		if (userOptional.isEmpty()) {
//			return new AuthResponse(false, "User not found");
//		}
//
//		User user = userOptional.get();
//		if (request.getName() != null)
//			user.setName(request.getName());
//		if (request.getPhone() != null)
//			user.setPhone(request.getPhone());
//		if (request.getDob() != null)
//			user.setDob(request.getDob());
//		if (request.getGender() != null)
//			user.setGender(request.getGender());
//		if (request.getAddressLine1() != null)
//			user.setAddressLine1(request.getAddressLine1());
//		if (request.getAddressLine2() != null)
//			user.setAddressLine2(request.getAddressLine2());
//
//		userRepository.save(user);
//		return new AuthResponse(true, null, "Profile Updated");
//	}

	public AuthResponse updateUserProfile(Long userId, UpdateProfileRequest request, MultipartFile imageFile) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isEmpty()) {
			return new AuthResponse(false, "User not found");
		}

		User user = userOptional.get();
		if (request.getName() != null)
			user.setName(request.getName());
		if (request.getPhone() != null)
			user.setPhone(request.getPhone());
		if (request.getDob() != null)
			user.setDob(request.getDob());
		if (request.getGender() != null)
			user.setGender(request.getGender());
		if (request.getAddressLine1() != null)
			user.setAddressLine1(request.getAddressLine1());
		if (request.getAddressLine2() != null)
			user.setAddressLine2(request.getAddressLine2());

		// Upload new profile image to Cloudinary if provided
		if (imageFile != null && !imageFile.isEmpty()) {
			String imageUrl = cloudinaryService.uploadImage(imageFile);
			user.setImage(imageUrl);
		}

		userRepository.save(user);
		return new AuthResponse(true, null, "Profile Updated");
	}

	// Book Appointment
	public AuthResponse bookAppointment(Long userId, BookAppointmentRequest request) {
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<Doctor> doctorOptional = doctorRepository.findById(request.getDocId());

		if (userOptional.isEmpty() || doctorOptional.isEmpty()) {
			return new AuthResponse(false, "User or Doctor not found");
		}

		Doctor doctor = doctorOptional.get();
		if (Boolean.FALSE.equals(doctor.getAvailable())) {
			return new AuthResponse(false, "Doctor not available");
		}

		// Trim strings to prevent whitespace discrepancies
		String cleanSlotDate = request.getSlotDate().trim();
		String cleanSlotTime = request.getSlotTime().trim();

		// Check if slot is already booked for this doctor
		boolean isSlotBooked = appointmentRepository
				.existsByDoctorIdAndSlotDateAndSlotTimeAndCancelledFalse(doctor.getId(), cleanSlotDate, cleanSlotTime);

		if (isSlotBooked) {
			return new AuthResponse(false, "Slot not available");
		}

		Appointment appointment = new Appointment();
		appointment.setUser(userOptional.get());
		appointment.setDoctor(doctor);
		appointment.setSlotDate(request.getSlotDate());
		appointment.setSlotTime(request.getSlotTime());
		appointment.setAmount(doctor.getFees());

		appointmentRepository.save(appointment);
		return new AuthResponse(true, null, "Appointment Booked");
	}

	// Get User Appointments
	public List<Appointment> getUserAppointments(Long userId) {
		List<Appointment> appointments = appointmentRepository.findByUserId(userId);
		Collections.reverse(appointments); // Latest appointments first
		return appointments;
	}

	// Cancel Appointment
	public AuthResponse cancelAppointment(Long userId, Map<String, Long> payload) {
		Long appointmentId = payload.get("appointmentId");
		Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

		if (appointmentOpt.isEmpty()) {
			return new AuthResponse(false, "Appointment not found");
		}

		Appointment appointment = appointmentOpt.get();
		if (!appointment.getUser().getId().equals(userId)) {
			return new AuthResponse(false, "Unauthorized Action");
		}

		appointment.setCancelled(true);
		appointmentRepository.save(appointment);

		return new AuthResponse(true, null, "Appointment Cancelled");
	}
}