package prescripto.backend.controller;

import prescripto.backend.dto.*;
import prescripto.backend.entity.Appointment;
import prescripto.backend.entity.User;
import prescripto.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Helper method to retrieve extracted User ID from Security Context
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String subject = (String) request.getAttribute("authenticatedSubject");
        return subject != null ? Long.parseLong(subject) : null;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        AuthResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        User user = userService.getUserProfile(userId);
        
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("success", true);
            response.put("userData", user);
        } else {
            response.put("success", false);
            response.put("message", "User not found");
        }
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/update-profile")
//    public ResponseEntity<AuthResponse> updateProfile(HttpServletRequest request,
//                                                      @RequestBody UpdateProfileRequest profileRequest) {
//        Long userId = getUserIdFromRequest(request);
//        AuthResponse response = userService.updateUserProfile(userId, profileRequest);
//        return ResponseEntity.ok(response);
//    }
    
    @PostMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> updateProfile(
            HttpServletRequest request,
            @RequestPart("userData") UpdateProfileRequest profileRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        
        Long userId = getUserIdFromRequest(request);
        AuthResponse response = userService.updateUserProfile(userId, profileRequest, imageFile);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<AuthResponse> bookAppointment(HttpServletRequest request,
                                                         @RequestBody BookAppointmentRequest bookRequest) {
        Long userId = getUserIdFromRequest(request);
        AuthResponse response = userService.bookAppointment(userId, bookRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointments")
    public ResponseEntity<Map<String, Object>> getUserAppointments(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        List<Appointment> appointments = userService.getUserAppointments(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("appointments", appointments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel-appointment")
    public ResponseEntity<AuthResponse> cancelAppointment(HttpServletRequest request,
                                                           @RequestBody Map<String, Long> payload) {
        Long userId = getUserIdFromRequest(request);
        AuthResponse response = userService.cancelAppointment(userId, payload);
        return ResponseEntity.ok(response);
    }
}