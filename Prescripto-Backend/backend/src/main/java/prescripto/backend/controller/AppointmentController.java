package prescripto.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import prescripto.backend.dto.*;
import prescripto.backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired private UserService userService;
    @Autowired private DoctorService doctorService;
    @Autowired private AdminService adminService;

    private Long getId(HttpServletRequest request) {
        String sub = (String) request.getAttribute("authenticatedSubject");
        return sub != null && !sub.contains("@") ? Long.parseLong(sub) : null;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getAppointments(HttpServletRequest request, Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        if ("ROLE_ADMIN".equals(role)) {
            response.put("appointments", adminService.getAllAppointments());
        } else if ("ROLE_DOCTOR".equals(role)) {
            response.put("appointments", doctorService.getDoctorAppointments(getId(request)));
        } else {
            response.put("appointments", userService.getUserAppointments(getId(request)));
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AuthResponse> bookAppointment(HttpServletRequest request, @RequestBody BookAppointmentRequest bookRequest) {
        return ResponseEntity.ok(userService.bookAppointment(getId(request), bookRequest));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AuthResponse> completeAppointment(HttpServletRequest request, @PathVariable("id") Long id) {
        Map<String, Long> payload = new HashMap<>();
        payload.put("appointmentId", id);
        return ResponseEntity.ok(doctorService.completeAppointment(getId(request), payload));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<AuthResponse> cancelAppointment(HttpServletRequest request, Authentication auth, @PathVariable("id") Long id) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        Map<String, Long> payload = new HashMap<>();
        payload.put("appointmentId", id);
        
        if ("ROLE_ADMIN".equals(role)) {
            return ResponseEntity.ok(adminService.cancelAppointmentByAdmin(payload));
        } else if ("ROLE_DOCTOR".equals(role)) {
            return ResponseEntity.ok(doctorService.cancelAppointmentByDoctor(getId(request), payload));
        } else {
            return ResponseEntity.ok(userService.cancelAppointment(getId(request), payload));
        }
    }
}
