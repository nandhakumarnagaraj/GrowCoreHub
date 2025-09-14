package com.growcorehub.controller;

import com.growcorehub.dto.LoginRequest;
import com.growcorehub.dto.RegisterRequest;
import com.growcorehub.entity.User;
import com.growcorehub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
		try {
			User user = userService.createUser(registerRequest);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "User registered successfully");
			response.put("userId", user.getId());
			response.put("email", user.getEmail());
			response.put("fullName", user.getFullName());

			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());

			if (userOpt.isPresent()) {
				User user = userOpt.get();
				// Note: In production, use proper password hashing and verification
				if (user.getPassword().equals(loginRequest.getPassword())) {
					Map<String, Object> response = new HashMap<>();
					response.put("success", true);
					response.put("message", "Login successful");
					response.put("userId", user.getId());
					response.put("email", user.getEmail());
					response.put("fullName", user.getFullName());
					response.put("isActive", user.getIsActive());
					response.put("emailVerified", user.getEmailVerified());

					return ResponseEntity.ok(response);
				}
			}

			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Invalid email or password");
			return ResponseEntity.badRequest().body(response);

		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Login failed: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/verify-email")
	public ResponseEntity<?> verifyEmail(@RequestParam String email) {
		try {
			Optional<User> userOpt = userService.findByEmail(email);
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				user.setEmailVerified(true);
				userService.updateUser(user.getId(), user);

				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				response.put("message", "Email verified successfully");
				return ResponseEntity.ok(response);
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("success", false);
				response.put("message", "User not found");
				return ResponseEntity.badRequest().body(response);
			}
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Email verification failed: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}
