package com.growcorehub.repository;

import com.growcorehub.entity.UserAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAssessmentRepository extends JpaRepository<UserAssessment, Long> {
	
	Optional<UserAssessment> findByUserIdAndAssessmentId(Long userId, Long assessmentId);

	List<UserAssessment> findByUserId(Long userId);

	List<UserAssessment> findByAssessmentId(Long assessmentId);
}