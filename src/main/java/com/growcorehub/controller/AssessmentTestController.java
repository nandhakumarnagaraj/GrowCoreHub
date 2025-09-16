package com.growcorehub.controller;

import com.growcorehub.dto.AssessmentDto;
import com.growcorehub.entity.Assessment;
import com.growcorehub.entity.UserAssessment;
import com.growcorehub.service.AssessmentTestService;
import com.growcorehub.utils.AssessmentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assessment-test")
@CrossOrigin(origins = "*")
public class AssessmentTestController {

    @Autowired
    private AssessmentTestService assessmentTestService;

    @PostMapping("/create-sample-java/{projectId}")
    public ResponseEntity<?> createSampleJavaAssessment(@PathVariable Long projectId) {
        try {
            Assessment assessment = assessmentTestService.createSampleJavaAssessment(projectId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sample Java assessment created successfully");
            response.put("assessment", new AssessmentDto(assessment));
            response.put("totalPoints", AssessmentUtils.calculateTotalPoints(assessment.getQuestions()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating sample assessment: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create-custom/{projectId}")
    public ResponseEntity<?> createCustomAssessment(@PathVariable Long projectId, @RequestParam String assessmentType) {
        try {
            Assessment assessment = assessmentTestService.createCustomAssessment(projectId, assessmentType);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Custom assessment created successfully");
            response.put("assessment", new AssessmentDto(assessment));
            response.put("type", assessmentType);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating custom assessment: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/submit-sample-answers/{assessmentId}")
    public ResponseEntity<?> submitSampleAnswers(@PathVariable Long assessmentId, @RequestParam Long userId,
            @RequestParam(defaultValue = "good") String answerQuality) {
        try {
            UserAssessment userAssessment = assessmentTestService.submitSampleAnswers(userId, assessmentId, answerQuality);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sample answers submitted successfully");
            response.put("userAssessmentId", userAssessment.getId());
            response.put("score", userAssessment.getScore());
            response.put("completedAt", userAssessment.getCompletedAt());
            response.put("answerQuality", answerQuality);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error submitting sample answers: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/validate-questions")
    public ResponseEntity<?> validateQuestionsJson(@RequestBody Map<String, String> request) {
        try {
            String questionsJson = request.get("questions");
            Map<String, Object> validationResult = assessmentTestService.validateQuestionsJson(questionsJson);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.putAll(validationResult);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error validating questions: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/validate-answers")
    public ResponseEntity<?> validateAnswersJson(@RequestBody Map<String, String> request) {
        try {
            String answersJson = request.get("answers");
            Map<String, Object> validationResult = assessmentTestService.validateAnswersJson(answersJson);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.putAll(validationResult);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error validating answers: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}