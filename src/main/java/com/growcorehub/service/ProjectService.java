package com.growcorehub.service;

import com.growcorehub.dto.ProjectDto;
import com.growcorehub.entity.Project;
import com.growcorehub.entity.ProjectApplication;
import com.growcorehub.entity.User;
import com.growcorehub.repository.ProjectRepository;
import com.growcorehub.repository.ProjectApplicationRepository;
import com.growcorehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectApplicationRepository projectApplicationRepository;

	@Autowired
	private UserRepository userRepository;

	public Project createProject(ProjectDto projectDto) {
		Project project = new Project();
		project.setTitle(projectDto.getTitle());
		project.setDescription(projectDto.getDescription());
		project.setCategory(projectDto.getCategory());
		project.setTermsConditions(projectDto.getTermsConditions());
		project.setScopeOfWork(projectDto.getScopeOfWork());
		project.setRequiredSkills(projectDto.getRequiredSkills());
		project.setMinimumScore(projectDto.getMinimumScore());
		project.setClientCrmUrl(projectDto.getClientCrmUrl());

		return projectRepository.save(project);
	}

	public Page<ProjectDto> getAllActiveProjects(Pageable pageable) {
		Page<Project> projects = projectRepository.findByStatusOrderByCreatedAtDesc(Project.ProjectStatus.ACTIVE,
				pageable);
		return projects.map(ProjectDto::new);
	}

	public Page<ProjectDto> getProjectsByCategory(String category, Pageable pageable) {
		Page<Project> projects = projectRepository.findByStatusAndCategoryContaining(Project.ProjectStatus.ACTIVE,
				category, pageable);
		return projects.map(ProjectDto::new);
	}

	public Optional<ProjectDto> getProjectById(Long id) {
		return projectRepository.findById(id).map(ProjectDto::new);
	}

	public Project updateProject(Long id, ProjectDto projectDto) {
		Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

		project.setTitle(projectDto.getTitle());
		project.setDescription(projectDto.getDescription());
		project.setCategory(projectDto.getCategory());
		project.setTermsConditions(projectDto.getTermsConditions());
		project.setScopeOfWork(projectDto.getScopeOfWork());
		project.setRequiredSkills(projectDto.getRequiredSkills());
		project.setMinimumScore(projectDto.getMinimumScore());
		project.setClientCrmUrl(projectDto.getClientCrmUrl());

		return projectRepository.save(project);
	}

	public ProjectApplication applyToProject(Long userId, Long projectId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		// Check if user already applied
		Optional<ProjectApplication> existingApplication = projectApplicationRepository.findByUserIdAndProjectId(userId,
				projectId);

		if (existingApplication.isPresent()) {
			throw new RuntimeException("User has already applied to this project");
		}

		ProjectApplication application = new ProjectApplication(user, project);
		return projectApplicationRepository.save(application);
	}

	public List<ProjectApplication> getUserApplications(Long userId) {
		return projectApplicationRepository.findByUserId(userId);
	}

	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}
}
