package com.growcorehub.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_assessments")
public class UserAssessment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name = "assessment_id", nullable = false)
	@JsonIgnore
	private Assessment assessment;

	@Column(nullable = false, precision = 5, scale = 2)
	private BigDecimal score;

	@Column(columnDefinition = "JSON")
	private String answers;

	@CreationTimestamp
	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	// Default constructor
	public UserAssessment() {
	}

	// Constructor
	public UserAssessment(User user, Assessment assessment, BigDecimal score, String answers) {
		this.user = user;
		this.assessment = assessment;
		this.score = score;
		this.answers = answers;
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

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}
}