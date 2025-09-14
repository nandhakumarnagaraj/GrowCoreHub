package com.growcorehub.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@Column(name = "aadhaar_number", length = 12)
	private String aadhaarNumber;

	@Column(length = 500)
	private String education;

	@Column(columnDefinition = "TEXT")
	private String skills;

	@Column(name = "experience_years")
	private Integer experienceYears = 0;

	@Column(name = "profile_completed")
	private Boolean profileCompleted = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "verification_status")
	private VerificationStatus verificationStatus = VerificationStatus.PENDING;

	@Column(name = "verification_documents", columnDefinition = "JSON")
	private String verificationDocuments;

	// Default constructor
	public UserProfile() {
	}

	// Constructor
	public UserProfile(User user) {
		this.user = user;
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

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public Integer getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(Integer experienceYears) {
		this.experienceYears = experienceYears;
	}

	public Boolean getProfileCompleted() {
		return profileCompleted;
	}

	public void setProfileCompleted(Boolean profileCompleted) {
		this.profileCompleted = profileCompleted;
	}

	public VerificationStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(VerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getVerificationDocuments() {
		return verificationDocuments;
	}

	public void setVerificationDocuments(String verificationDocuments) {
		this.verificationDocuments = verificationDocuments;
	}

	// Enum for verification status
	public enum VerificationStatus {
		PENDING, VERIFIED, REJECTED
	}
}