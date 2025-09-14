package com.growcorehub.dto;

import com.growcorehub.entity.Assessment;
import java.math.BigDecimal;

public class AssessmentDto {

	private Long id;
	private Long projectId;
	private String name;
	private String description;
	private String questions;
	private BigDecimal maxScore;
	private Integer timeLimitMinutes;

	// Constructors
	public AssessmentDto() {
	}

	public AssessmentDto(Assessment assessment) {
		this.id = assessment.getId();
		this.projectId = assessment.getProject().getId();
		this.name = assessment.getName();
		this.description = assessment.getDescription();
		this.questions = assessment.getQuestions();
		this.maxScore = assessment.getMaxScore();
		this.timeLimitMinutes = assessment.getTimeLimitMinutes();
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public BigDecimal getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(BigDecimal maxScore) {
		this.maxScore = maxScore;
	}

	public Integer getTimeLimitMinutes() {
		return timeLimitMinutes;
	}

	public void setTimeLimitMinutes(Integer timeLimitMinutes) {
		this.timeLimitMinutes = timeLimitMinutes;
	}
}