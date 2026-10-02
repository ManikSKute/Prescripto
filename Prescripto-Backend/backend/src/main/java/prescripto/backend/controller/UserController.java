package prescripto.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prescripto.backend.dto.*;
import prescripto.backend.entity.User;
import prescripto.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	private Long getUserId(HttpServletRequest request) {
		return Long.parseLong((String) request.getAttribute("authenticatedSubject"));
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request) {
		User user = userService.getUserProfile(getUserId(request));
		Map<String, Object> response = new HashMap<>();
		response.put("success", user != null);
		if (user != null)
			response.put("userData", user);
		else
			response.put("message", "User not found");
		return ResponseEntity.ok(response);
	}

	@PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<AuthResponse> updateProfile(HttpServletRequest request,
			@RequestPart("userData") UpdateProfileRequest profileRequest,
			@RequestPart(value = "image", required = false) MultipartFile imageFile) {
		return ResponseEntity.ok(userService.updateUserProfile(getUserId(request), profileRequest, imageFile));
	}
}
