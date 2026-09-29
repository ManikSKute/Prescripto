package prescripto.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prescripto.backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired private AdminService adminService;
    @Autowired private DoctorService doctorService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("dashData", adminService.getAdminDashboardData());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> getDoctorDashboard(HttpServletRequest request) {
        Long id = Long.parseLong((String) request.getAttribute("authenticatedSubject"));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("dashData", doctorService.getDoctorDashboardData(id));
        return ResponseEntity.ok(response);
    }
}
