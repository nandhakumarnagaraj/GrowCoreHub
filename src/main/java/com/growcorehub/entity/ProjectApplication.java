package com.growcorehub.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_applications")
public class ProjectApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	@JsonIgnore
	private Project project;

	@Enumerated(EnumType.STRING)
	@Column(name = "application_status")
	private ApplicationStatus applicationStatus = ApplicationStatus.APPLIED;

	@Column(name = "assessment_score", precision = 5, scale = 2)
	private BigDecimal assessmentScore;

	@CreationTimestamp
	@Column(name = "applied_at")
	private LocalDateTime appliedAt;

	@Column(name = "agreement_signed")
	private Boolean agreementSigned = false;

	@Column(name = "agreement_signed_at")
	private LocalDateTime agreementSignedAt;

	// Default constructor
	public ProjectApplication() {
	}

	// Constructor
	public ProjectApplication(User user, Project project) {
		this.user = user;
		this.project = project;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public BigDecimal getAssessmentScore() {
		return assessmentScore;
	}

	public void setAssessmentScore(BigDecimal assessmentScore) {
		this.assessmentScore = assessmentScore;
	}

	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}

	public Boolean getAgreementSigned() {
		return agreementSigned;
	}

	public void setAgreementSigned(Boolean agreementSigned) {
		this.agreementSigned = agreementSigned;
	}

	public LocalDateTime getAgreementSignedAt() {
		return agreementSignedAt;
	}

	public void setAgreementSignedAt(LocalDateTime agreementSignedAt) {
		this.agreementSignedAt = agreementSignedAt;
	}

	// Enum for application status
	public enum ApplicationStatus {
		APPLIED, ACCEPTED, REJECTED, IN_PROGRESS, COMPLETED
	}
}