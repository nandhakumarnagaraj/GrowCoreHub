package com.growcorehub.controller;

import com.growcorehub.dto.UserProfileDto;
import com.growcorehub.entity.User;
import com.growcorehub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			Map<String, Object> response = new HashMap<>();
			User u = user.get();
			response.put("success", true);
			response.put("user",
					Map.of("id", u.getId(), "email", u.getEmail(), "firstName", u.getFirstName(), "lastName",
							u.getLastName(), "phone", u.getPhone(), "isActive", u.getIsActive(), "emailVerified",
							u.getEmailVerified(), "createdAt", u.getCreatedAt()));
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		List<User> users = userService.getAllUsers();
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("users", users);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}/profile")
	public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
		UserProfileDto profile = userService.getUserProfile(id);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("profile", profile);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/profile")
	public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto profileDto) {
		UserProfileDto updatedProfile = userService.updateUserProfile(id, profileDto);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Profile updated successfully");
		response.put("profile", updatedProfile);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
		User updatedUser = userService.updateUser(id, userDetails);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "User updated successfully");
		response.put("user", updatedUser);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "User deleted successfully");
		return ResponseEntity.ok(response);
	}
}