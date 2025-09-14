package com.growcorehub.dto;

import com.growcorehub.entity.Project;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProjectDto {

	private Long id;
	private String title;
	private String description;
	private String category;
	private String termsConditions;
	private String scopeOfWork;
	private String requiredSkills;
	private BigDecimal minimumScore;
	private Project.ProjectStatus status;
	private LocalDateTime createdAt;
	private String clientCrmUrl;

	// Constructors
	public ProjectDto() {
	}

	public ProjectDto(Project project) {
		this.id = project.getId();
		this.title = project.getTitle();
		this.description = project.getDescription();
		this.category = project.getCategory();
		this.termsConditions = project.getTermsConditions();
		this.scopeOfWork = project.getScopeOfWork();
		this.requiredSkills = project.getRequiredSkills();
		this.minimumScore = project.getMinimumScore();
		this.status = project.getStatus();
		this.createdAt = project.getCreatedAt();
		this.clientCrmUrl = project.getClientCrmUrl();
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

	public Project.ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(Project.ProjectStatus status) {
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
}
