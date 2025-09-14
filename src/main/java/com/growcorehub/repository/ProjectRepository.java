package com.growcorehub.repository;

import com.growcorehub.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Page<Project> findByStatusOrderByCreatedAtDesc(Project.ProjectStatus status, Pageable pageable);

	@Query("SELECT p FROM Project p WHERE p.status = :status AND "
			+ "(p.category LIKE %:category% OR :category IS NULL)")
	Page<Project> findByStatusAndCategoryContaining(@Param("status") Project.ProjectStatus status,
			@Param("category") String category, Pageable pageable);
}