package com.growcorehub.repository;

import com.growcorehub.entity.ProjectApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
	
	Optional<ProjectApplication> findByUserIdAndProjectId(Long userId, Long projectId);

	List<ProjectApplication> findByUserId(Long userId);

	List<ProjectApplication> findByProjectId(Long projectId);

	List<ProjectApplication> findByApplicationStatus(ProjectApplication.ApplicationStatus status);
}