package com.growcorehub.controller;

import com.growcorehub.dto.AssessmentDto;
import com.growcorehub.entity.Assessment;
import com.growcorehub.entity.UserAssessment;
import com.growcorehub.service.AssessmentService;
import com.growcorehub.service.AssessmentService.AssessmentStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
		try {
			Assessment assessment = assessmentService.createAssessment(assessmentDto);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Assessment created successfully");
			response.put("assessment", new AssessmentDto(assessment));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error creating assessment: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/project/{projectId}")
	public ResponseEntity<?> getAssessmentsByProject(@PathVariable Long projectId) {
		try {
			List<AssessmentDto> assessments = assessmentService.getAssessmentsByProjectId(projectId);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("assessments", assessments);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching assessments: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAssessmentById(@PathVariable Long id) {
		try {
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
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching assessment: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * Submit assessment with automatic score calculation
	 * Request body should contain: { "answers": "{\"q1\":\"answer1\",\"q2\":\"answer2\"}" }
	 */
	@PostMapping("/{assessmentId}/submit")
	public ResponseEntity<?> submitAssessment(
			@PathVariable Long assessmentId, 
			@RequestParam Long userId,
			@RequestBody Map<String, Object> submissionData) {
		
		try {
			String answers = (String) submissionData.get("answers");
			
			if (answers == null || answers.trim().isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("success", false);
				response.put("message", "Answers are required");
				return ResponseEntity.badRequest().body(response);
			}

			UserAssessment userAssessment = assessmentService.submitAssessment(userId, assessmentId, answers);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Assessment submitted successfully");
			response.put("userAssessmentId", userAssessment.getId());
			response.put("score", userAssessment.getScore());
			response.put("completedAt", userAssessment.getCompletedAt());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error submitting assessment: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * Alternative endpoint for backward compatibility
	 * Accepts answers as a request parameter
	 */
	@PostMapping("/{assessmentId}/submit-legacy")
	public ResponseEntity<?> submitAssessmentLegacy(
			@PathVariable Long assessmentId, 
			@RequestParam Long userId,
			@RequestParam String answers) {
		
		try {
			UserAssessment userAssessment = assessmentService.submitAssessment(userId, assessmentId, answers);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Assessment submitted successfully");
			response.put("userAssessmentId", userAssessment.getId());
			response.put("score", userAssessment.getScore());
			response.put("completedAt", userAssessment.getCompletedAt());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error submitting assessment: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUserAssessments(@PathVariable Long userId) {
		try {
			List<UserAssessment> assessments = assessmentService.getUserAssessments(userId);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("userAssessments", assessments);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching user assessments: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/result/{userId}/{assessmentId}")
	public ResponseEntity<?> getAssessmentResult(@PathVariable Long userId, @PathVariable Long assessmentId) {
		try {
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
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching assessment result: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * Get assessment statistics (admin endpoint)
	 */
	@GetMapping("/{assessmentId}/statistics")
	public ResponseEntity<?> getAssessmentStatistics(@PathVariable Long assessmentId) {
		try {
			AssessmentStatistics statistics = assessmentService.getAssessmentStatistics(assessmentId);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("statistics", statistics);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching assessment statistics: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAssessment(@PathVariable Long id) {
		try {
			assessmentService.deleteAssessment(id);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Assessment deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error deleting assessment: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}