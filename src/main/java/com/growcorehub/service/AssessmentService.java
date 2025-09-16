package com.growcorehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growcorehub.dto.AssessmentDto;
import com.growcorehub.entity.Assessment;
import com.growcorehub.entity.Project;
import com.growcorehub.entity.User;
import com.growcorehub.entity.UserAssessment;
import com.growcorehub.repository.AssessmentRepository;
import com.growcorehub.repository.ProjectRepository;
import com.growcorehub.repository.UserAssessmentRepository;
import com.growcorehub.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private UserAssessmentRepository userAssessmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Assessment createAssessment(AssessmentDto assessmentDto) {
        Project project = projectRepository.findById(assessmentDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Assessment assessment = new Assessment();
        assessment.setProject(project);
        assessment.setName(assessmentDto.getName());
        assessment.setDescription(assessmentDto.getDescription());
        assessment.setQuestions(assessmentDto.getQuestions());
        assessment.setMaxScore(assessmentDto.getMaxScore());
        assessment.setTimeLimitMinutes(assessmentDto.getTimeLimitMinutes());

        return assessmentRepository.save(assessment);
    }

    public List<AssessmentDto> getAssessmentsByProjectId(Long projectId) {
        List<Assessment> assessments = assessmentRepository.findByProjectId(projectId);
        return assessments.stream().map(AssessmentDto::new).collect(Collectors.toList());
    }

    public Optional<AssessmentDto> getAssessmentById(Long id) {
        return assessmentRepository.findById(id).map(AssessmentDto::new);
    }

    public UserAssessment submitAssessment(Long userId, Long assessmentId, String answers, BigDecimal score) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        Optional<UserAssessment> existingAssessment = userAssessmentRepository.findByUserIdAndAssessmentId(userId,
                assessmentId);

        if (existingAssessment.isPresent()) {
            throw new RuntimeException("User has already taken this assessment");
        }

        UserAssessment userAssessment = new UserAssessment(user, assessment, score, answers);
        return userAssessmentRepository.save(userAssessment);
    }

    public List<UserAssessment> getUserAssessments(Long userId) {
        return userAssessmentRepository.findByUserId(userId);
    }

    public Optional<UserAssessment> getUserAssessmentResult(Long userId, Long assessmentId) {
        return userAssessmentRepository.findByUserIdAndAssessmentId(userId, assessmentId);
    }

    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }
}