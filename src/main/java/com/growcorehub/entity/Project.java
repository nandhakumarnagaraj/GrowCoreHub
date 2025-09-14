package com.growcorehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(length = 100)
	private String category;

	@Column(name = "terms_conditions", columnDefinition = "TEXT")
	private String termsConditions;

	@Column(name = "scope_of_work", columnDefinition = "TEXT")
	private String scopeOfWork;

	@Column(name = "required_skills", columnDefinition = "JSON")
	private String requiredSkills;

	@Column(name = "minimum_score", precision = 5, scale = 2)
	private BigDecimal minimumScore = new BigDecimal("70.00");

	@Enumerated(EnumType.STRING)
	private ProjectStatus status = ProjectStatus.ACTIVE;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "client_crm_url", length = 500)
	private String clientCrmUrl;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Assessment> assessments;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProjectApplication> applications;

	// Default constructor
	public Project() {
	}

	// Constructor
	public Project(String title, String description, String category) {
		this.title = title;
		this.description = description;
		this.category = category;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTermsConditions() {
		return termsConditions;
	}

	public void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public String getScopeOfWork() {
		return scopeOfWork;
	}

	public void setScopeOfWork(String scopeOfWork) {
		this.scopeOfWork = scopeOfWork;
	}

	public String getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(String requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public BigDecimal getMinimumScore() {
		return minimumScore;
	}

	public void setMinimumScore(BigDecimal minimumScore) {
		this.minimumScore = minimumScore;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getClientCrmUrl() {
		return clientCrmUrl;
	}

	public void setClientCrmUrl(String clientCrmUrl) {
		this.clientCrmUrl = clientCrmUrl;
	}

	public List<Assessment> getAssessments() {
		return assessments;
	}

	public void setAssessments(List<Assessment> assessments) {
		this.assessments = assessments;
	}

	public List<ProjectApplication> getApplications() {
		return applications;
	}

	public void setApplications(List<ProjectApplication> applications) {
		this.applications = applications;
	}

	// Enum for project status
	public enum ProjectStatus {
		ACTIVE, INACTIVE, COMPLETED
	}
}