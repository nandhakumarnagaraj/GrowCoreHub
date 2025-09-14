package com.growcorehub.controller;

import com.growcorehub.entity.ProjectApplication;
import com.growcorehub.entity.UserAssessment;
import com.growcorehub.service.AssessmentService;
import com.growcorehub.service.ProjectService;
import com.growcorehub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AssessmentService assessmentService;

	@GetMapping("/summary/{userId}")
	public ResponseEntity<?> getDashboardSummary(@PathVariable Long userId) {
		try {
			// Get user applications
			List<ProjectApplication> applications = projectService.getUserApplications(userId);

			// Get user assessments
			List<UserAssessment> assessments = assessmentService.getUserAssessments(userId);

			// Calculate statistics
			long totalApplications = applications.size();
			long acceptedApplications = applications.stream()
					.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.ACCEPTED).count();
			long inProgressApplications = applications.stream()
					.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.IN_PROGRESS)
					.count();
			long completedApplications = applications.stream()
					.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.COMPLETED)
					.count();

			// Calculate average assessment score
			OptionalDouble averageScore = assessments.stream()
					.mapToDouble(assessment -> assessment.getScore().doubleValue()).average();

			// Get highest score
			OptionalDouble highestScore = assessments.stream()
					.mapToDouble(assessment -> assessment.getScore().doubleValue()).max();

			Map<String, Object> summary = new HashMap<>();
			summary.put("totalApplications", totalApplications);
			summary.put("acceptedApplications", acceptedApplications);
			summary.put("inProgressApplications", inProgressApplications);
			summary.put("completedApplications", completedApplications);
			summary.put("totalAssessments", assessments.size());
			summary.put("averageScore",
					averageScore.isPresent() ? BigDecimal.valueOf(averageScore.getAsDouble()) : BigDecimal.ZERO);
			summary.put("highestScore",
					highestScore.isPresent() ? BigDecimal.valueOf(highestScore.getAsDouble()) : BigDecimal.ZERO);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("summary", summary);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching dashboard summary: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/performance/{userId}")
	public ResponseEntity<?> getUserPerformance(@PathVariable Long userId) {
		try {
			List<UserAssessment> assessments = assessmentService.getUserAssessments(userId);
			List<ProjectApplication> applications = projectService.getUserApplications(userId);

			Map<String, Object> performance = new HashMap<>();
			performance.put("assessments", assessments);
			performance.put("applications", applications);

			// Calculate performance metrics
			Map<String, Long> applicationStats = new HashMap<>();
			applicationStats.put("applied", applications.stream()
					.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.APPLIED).count());
			applicationStats.put("accepted",
					applications.stream()
							.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.ACCEPTED)
							.count());
			applicationStats.put("rejected",
					applications.stream()
							.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.REJECTED)
							.count());
			applicationStats.put("inProgress",
					applications.stream().filter(
							app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.IN_PROGRESS)
							.count());
			applicationStats.put("completed",
					applications.stream()
							.filter(app -> app.getApplicationStatus() == ProjectApplication.ApplicationStatus.COMPLETED)
							.count());

			performance.put("applicationStats", applicationStats);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("performance", performance);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching performance data: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/recent-activity/{userId}")
	public ResponseEntity<?> getRecentActivity(@PathVariable Long userId) {
		try {
			// Get recent applications (last 10)
			List<ProjectApplication> recentApplications = projectService.getUserApplications(userId).stream()
					.sorted((a, b) -> b.getAppliedAt().compareTo(a.getAppliedAt())).limit(10).toList();

			// Get recent assessments (last 10)
			List<UserAssessment> recentAssessments = assessmentService.getUserAssessments(userId).stream()
					.sorted((a, b) -> b.getCompletedAt().compareTo(a.getCompletedAt())).limit(10).toList();

			Map<String, Object> activity = new HashMap<>();
			activity.put("recentApplications", recentApplications);
			activity.put("recentAssessments", recentAssessments);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("activity", activity);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching recent activity: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}