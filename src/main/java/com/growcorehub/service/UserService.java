package com.growcorehub.service;

import com.growcorehub.dto.RegisterRequest;
import com.growcorehub.dto.UserProfileDto;
import com.growcorehub.entity.User;
import com.growcorehub.entity.UserProfile;
import com.growcorehub.repository.UserRepository;
import com.growcorehub.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User createUser(RegisterRequest registerRequest) {
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		User user = new User();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Hash password
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setPhone(registerRequest.getPhone());

		User savedUser = userRepository.save(user);

		// Create user profile
		UserProfile userProfile = new UserProfile(savedUser);
		userProfileRepository.save(userProfile);

		return savedUser;
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User updateUser(Long id, User userDetails) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setPhone(userDetails.getPhone());

		return userRepository.save(user);
	}

	public UserProfileDto getUserProfile(Long userId) {
		Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);
		return userProfile.map(UserProfileDto::new).orElse(null);
	}

	public UserProfileDto updateUserProfile(Long userId, UserProfileDto profileDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
		UserProfile userProfile;

		if (existingProfile.isPresent()) {
			userProfile = existingProfile.get();
		} else {
			userProfile = new UserProfile(user);
		}

		userProfile.setAadhaarNumber(profileDto.getAadhaarNumber());
		userProfile.setEducation(profileDto.getEducation());
		userProfile.setSkills(profileDto.getSkills());
		userProfile.setExperienceYears(profileDto.getExperienceYears());
		userProfile.setProfileCompleted(true);

		UserProfile savedProfile = userProfileRepository.save(userProfile);
		return new UserProfileDto(savedProfile);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}