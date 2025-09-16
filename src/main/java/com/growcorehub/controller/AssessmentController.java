package com.growcorehub.controller;

import com.growcorehub.dto.AssessmentDto;
import com.growcorehub.entity.Assessment;
import com.growcorehub.entity.UserAssessment;
import com.growcorehub.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/assessments")
@CrossOrigin(origins = "*")
public class AssessmentController {

	@Autowired
	private AssessmentService assessmentService;

	@PostMapping
	public ResponseEntity<?> createAssessment(@RequestBody AssessmentDto assessmentDto) {
		Assessment assessment = assessmentService.createAssessment(assessmentDto);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Assessment created successfully");
		response.put("assessment", new AssessmentDto(assessment));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/project/{projectId}")
	public ResponseEntity<?> getAssessmentsByProject(@PathVariable Long projectId) {
		List<AssessmentDto> assessments = assessmentService.getAssessmentsByProjectId(projectId);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("assessments", assessments);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAssessmentById(@PathVariable Long id) {
		Optional<AssessmentDto> assessment = assessmentService.getAssessmentById(id);
		if (assessment.isPresent()) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("assessment", assessment.get());
			return ResponseEntity.ok(response);
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Assessment not found");
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{assessmentId}/submit")
	public ResponseEntity<?> submitAssessment(@PathVariable Long assessmentId, @RequestParam Long userId,
			@RequestParam String answers, @RequestParam BigDecimal score) {

		UserAssessment userAssessment = assessmentService.submitAssessment(userId, assessmentId, answers, score);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Assessment submitted successfully");
		response.put("userAssessmentId", userAssessment.getId());
		response.put("score", userAssessment.getScore());
		response.put("completedAt", userAssessment.getCompletedAt());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUserAssessments(@PathVariable Long userId) {
		List<UserAssessment> assessments = assessmentService.getUserAssessments(userId);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("userAssessments", assessments);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/result/{userId}/{assessmentId}")
	public ResponseEntity<?> getAssessmentResult(@PathVariable Long userId, @PathVariable Long assessmentId) {

		Optional<UserAssessment> result = assessmentService.getUserAssessmentResult(userId, assessmentId);
		if (result.isPresent()) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("result", result.get());
			return ResponseEntity.ok(response);
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Assessment result not found");
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAssessment(@PathVariable Long id) {
		assessmentService.deleteAssessment(id);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Assessment deleted successfully");
		return ResponseEntity.ok(response);
	}
}