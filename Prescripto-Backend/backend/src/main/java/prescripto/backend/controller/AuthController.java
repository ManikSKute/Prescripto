package prescripto.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prescripto.backend.dto.*;
import prescripto.backend.service.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private UserService userService;
    @Autowired private DoctorService doctorService;
    @Autowired private AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }
    @PostMapping("/doctor-login")
    public ResponseEntity<AuthResponse> loginDoctor(@RequestBody DoctorLoginRequest request) {
        return ResponseEntity.ok(doctorService.loginDoctor(request));
    }
    @PostMapping("/admin-login")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(adminService.loginAdmin(request));
    }
}
