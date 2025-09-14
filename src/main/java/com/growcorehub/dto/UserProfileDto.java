package com.growcorehub.dto;

import com.growcorehub.entity.UserProfile;

public class UserProfileDto {

	private Long id;
	private Long userId;
	private String aadhaarNumber;
	private String education;
	private String skills;
	private Integer experienceYears;
	private Boolean profileCompleted;
	private UserProfile.VerificationStatus verificationStatus;
	private String verificationDocuments;

	// Constructors
	public UserProfileDto() {
	}

	public UserProfileDto(UserProfile userProfile) {
		this.id = userProfile.getId();
		this.userId = userProfile.getUser().getId();
		this.aadhaarNumber = userProfile.getAadhaarNumber();
		this.education = userProfile.getEducation();
		this.skills = userProfile.getSkills();
		this.experienceYears = userProfile.getExperienceYears();
		this.profileCompleted = userProfile.getProfileCompleted();
		this.verificationStatus = userProfile.getVerificationStatus();
		this.verificationDocuments = userProfile.getVerificationDocuments();
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public UserProfile.VerificationStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(UserProfile.VerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getVerificationDocuments() {
		return verificationDocuments;
	}

	public void setVerificationDocuments(String verificationDocuments) {
		this.verificationDocuments = verificationDocuments;
	}
}