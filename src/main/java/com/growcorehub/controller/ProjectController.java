package com.growcorehub.controller;

import com.growcorehub.dto.ProjectDto;
import com.growcorehub.entity.Project;
import com.growcorehub.entity.ProjectApplication;
import com.growcorehub.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@PostMapping
	public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
		try {
			Project project = projectService.createProject(projectDto);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Project created successfully");
			response.put("project", new ProjectDto(project));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error creating project: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllProjects(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String category) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<ProjectDto> projects;

			if (category != null && !category.trim().isEmpty()) {
				projects = projectService.getProjectsByCategory(category, pageable);
			} else {
				projects = projectService.getAllActiveProjects(pageable);
			}

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("projects", projects.getContent());
			response.put("currentPage", projects.getNumber());
			response.put("totalPages", projects.getTotalPages());
			response.put("totalElements", projects.getTotalElements());
			response.put("pageSize", projects.getSize());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching projects: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProjectById(@PathVariable Long id) {
		try {
			Optional<ProjectDto> project = projectService.getProjectById(id);
			if (project.isPresent()) {
				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				response.put("project", project.get());
				return ResponseEntity.ok(response);
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("success", false);
				response.put("message", "Project not found");
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching project: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/{projectId}/apply")
	public ResponseEntity<?> applyToProject(@PathVariable Long projectId, @RequestParam Long userId) {
		try {
			ProjectApplication application = projectService.applyToProject(userId, projectId);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Application submitted successfully");
			response.put("applicationId", application.getId());
			response.put("status", application.getApplicationStatus());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error applying to project: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/user/{userId}/applications")
	public ResponseEntity<?> getUserApplications(@PathVariable Long userId) {
		try {
			List<ProjectApplication> applications = projectService.getUserApplications(userId);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("applications", applications);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error fetching applications: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
		try {
			Project updatedProject = projectService.updateProject(id, projectDto);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Project updated successfully");
			response.put("project", new ProjectDto(updatedProject));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error updating project: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable Long id) {
		try {
			projectService.deleteProject(id);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Project deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error deleting project: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}