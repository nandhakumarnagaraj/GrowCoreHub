package com.growcorehub.service;

import com.growcorehub.dto.AssessmentDto;
import com.growcorehub.entity.Assessment;
import com.growcorehub.entity.Project;
import com.growcorehub.entity.User;
import com.growcorehub.entity.UserAssessment;
<<<<<<< HEAD
import com.growcorehub.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
=======
import com.growcorehub.repository.AssessmentRepository;
import com.growcorehub.repository.ProjectRepository;
import com.growcorehub.repository.UserAssessmentRepository;
import com.growcorehub.repository.UserRepository;
>>>>>>> parent of ea94e5f (Assessment)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

<<<<<<< HEAD
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Assessment createAssessment(AssessmentDto assessmentDto) {
        Project project = projectRepository.findById(assessmentDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
=======
	public Assessment createAssessment(AssessmentDto assessmentDto) {
		Project project = projectRepository.findById(assessmentDto.getProjectId())
				.orElseThrow(() -> new RuntimeException("Project not found"));
>>>>>>> parent of ea94e5f (Assessment)

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

<<<<<<< HEAD
    /**
     * Submit assessment with automatic score calculation
     * @param userId User ID
     * @param assessmentId Assessment ID
     * @param answers JSON string containing user answers
     * @return UserAssessment with calculated score
     */
    public UserAssessment submitAssessment(Long userId, Long assessmentId, String answers) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
=======
	public UserAssessment submitAssessment(Long userId, Long assessmentId, String answers, BigDecimal score) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
>>>>>>> parent of ea94e5f (Assessment)

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

<<<<<<< HEAD
        // Check if user already took this assessment
        Optional<UserAssessment> existingAssessment = userAssessmentRepository
                .findByUserIdAndAssessmentId(userId, assessmentId);
=======
		// Check if user already took this assessment
		Optional<UserAssessment> existingAssessment = userAssessmentRepository.findByUserIdAndAssessmentId(userId,
				assessmentId);
>>>>>>> parent of ea94e5f (Assessment)

        if (existingAssessment.isPresent()) {
            throw new RuntimeException("User has already taken this assessment");
        }

<<<<<<< HEAD
        // Calculate score based on answers
        BigDecimal calculatedScore = calculateScore(assessment, answers);

        UserAssessment userAssessment = new UserAssessment(user, assessment, calculatedScore, answers);
        return userAssessmentRepository.save(userAssessment);
    }

    /**
     * Calculate score based on assessment questions and user answers
     * Expected JSON format for questions:
     * [
     *   {
     *     "id": "q1",
     *     "question": "What is Java?",
     *     "type": "multiple_choice",
     *     "options": ["Language", "Framework", "Database", "OS"],
     *     "correctAnswer": "Language",
     *     "points": 10
     *   },
     *   {
     *     "id": "q2",
     *     "question": "Explain OOP concepts",
     *     "type": "text",
     *     "keywords": ["inheritance", "polymorphism", "encapsulation"],
     *     "points": 15
     *   }
     * ]
     * 
     * Expected JSON format for answers:
     * {
     *   "q1": "Language",
     *   "q2": "OOP includes inheritance, encapsulation, and polymorphism..."
     * }
     */
    private BigDecimal calculateScore(Assessment assessment, String answers) {
        try {
            JsonNode questionsNode = objectMapper.readTree(assessment.getQuestions());
            JsonNode answersNode = objectMapper.readTree(answers);
            
            double totalPoints = 0;
            double earnedPoints = 0;

            if (questionsNode.isArray()) {
                for (JsonNode questionNode : questionsNode) {
                    String questionId = questionNode.get("id").asText();
                    String questionType = questionNode.get("type").asText();
                    double points = questionNode.get("points").asDouble();
                    
                    totalPoints += points;

                    if (answersNode.has(questionId)) {
                        String userAnswer = answersNode.get(questionId).asText();
                        double questionScore = calculateQuestionScore(questionNode, userAnswer);
                        earnedPoints += (questionScore * points);
                    }
                }
            }

            // Calculate percentage score
            if (totalPoints > 0) {
                double percentage = (earnedPoints / totalPoints) * 100;
                return BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP);
            }
            
            return BigDecimal.ZERO;

        } catch (Exception e) {
            // If JSON parsing fails, return 0 score
            System.err.println("Error calculating score: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calculate score for individual question based on type
     */
    private double calculateQuestionScore(JsonNode questionNode, String userAnswer) {
        String questionType = questionNode.get("type").asText();
        
        switch (questionType.toLowerCase()) {
            case "multiple_choice":
            case "single_choice":
                return calculateMultipleChoiceScore(questionNode, userAnswer);
                
            case "text":
            case "essay":
                return calculateTextScore(questionNode, userAnswer);
                
            case "true_false":
            case "boolean":
                return calculateTrueFalseScore(questionNode, userAnswer);
                
            case "numerical":
            case "number":
                return calculateNumericalScore(questionNode, userAnswer);
                
            default:
                return 0.0;
        }
    }

    private double calculateMultipleChoiceScore(JsonNode questionNode, String userAnswer) {
        if (questionNode.has("correctAnswer")) {
            String correctAnswer = questionNode.get("correctAnswer").asText();
            return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim()) ? 1.0 : 0.0;
        }
        return 0.0;
    }

    private double calculateTrueFalseScore(JsonNode questionNode, String userAnswer) {
        if (questionNode.has("correctAnswer")) {
            String correctAnswer = questionNode.get("correctAnswer").asText();
            return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim()) ? 1.0 : 0.0;
        }
        return 0.0;
    }

    private double calculateNumericalScore(JsonNode questionNode, String userAnswer) {
        if (questionNode.has("correctAnswer")) {
            try {
                double correctAnswer = questionNode.get("correctAnswer").asDouble();
                double userNum = Double.parseDouble(userAnswer.trim());
                
                // Allow for small tolerance in numerical answers
                double tolerance = questionNode.has("tolerance") ? 
                    questionNode.get("tolerance").asDouble() : 0.01;
                
                return Math.abs(correctAnswer - userNum) <= tolerance ? 1.0 : 0.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    /**
     * Calculate score for text-based questions using keyword matching
     * This is a basic implementation - you might want to use more sophisticated
     * NLP techniques or manual grading for better accuracy
     */
    private double calculateTextScore(JsonNode questionNode, String userAnswer) {
        if (questionNode.has("keywords")) {
            JsonNode keywordsNode = questionNode.get("keywords");
            String answerLower = userAnswer.toLowerCase();
            
            int totalKeywords = 0;
            int foundKeywords = 0;
            
            if (keywordsNode.isArray()) {
                totalKeywords = keywordsNode.size();
                for (JsonNode keywordNode : keywordsNode) {
                    String keyword = keywordNode.asText().toLowerCase();
                    if (answerLower.contains(keyword)) {
                        foundKeywords++;
                    }
                }
            }
            
            if (totalKeywords > 0) {
                return (double) foundKeywords / totalKeywords;
            }
        }
        
        // If no keywords specified, give partial credit for non-empty answers
        return userAnswer.trim().length() > 10 ? 0.5 : 0.0;
    }

    /**
     * Get assessment statistics for a project
     */
    public AssessmentStatistics getAssessmentStatistics(Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        List<UserAssessment> submissions = userAssessmentRepository.findByAssessmentId(assessmentId);
        
        AssessmentStatistics stats = new AssessmentStatistics();
        stats.setTotalSubmissions(submissions.size());
        
        if (!submissions.isEmpty()) {
            double avgScore = submissions.stream()
                    .mapToDouble(ua -> ua.getScore().doubleValue())
                    .average()
                    .orElse(0.0);
            
            double maxScore = submissions.stream()
                    .mapToDouble(ua -> ua.getScore().doubleValue())
                    .max()
                    .orElse(0.0);
            
            double minScore = submissions.stream()
                    .mapToDouble(ua -> ua.getScore().doubleValue())
                    .min()
                    .orElse(0.0);
            
            long passedCount = submissions.stream()
                    .filter(ua -> ua.getScore().compareTo(assessment.getProject().getMinimumScore()) >= 0)
                    .count();
            
            stats.setAverageScore(BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP));
            stats.setMaximumScore(BigDecimal.valueOf(maxScore).setScale(2, RoundingMode.HALF_UP));
            stats.setMinimumScore(BigDecimal.valueOf(minScore).setScale(2, RoundingMode.HALF_UP));
            stats.setPassedCount(passedCount);
            stats.setPassRate((double) passedCount / submissions.size() * 100);
        }
        
        return stats;
    }

    public List<UserAssessment> getUserAssessments(Long userId) {
        return userAssessmentRepository.findByUserId(userId);
    }
=======
		UserAssessment userAssessment = new UserAssessment(user, assessment, score, answers);
		return userAssessmentRepository.save(userAssessment);
	}

	public List<UserAssessment> getUserAssessments(Long userId) {
		return userAssessmentRepository.findByUserId(userId);
	}
>>>>>>> parent of ea94e5f (Assessment)

    public Optional<UserAssessment> getUserAssessmentResult(Long userId, Long assessmentId) {
        return userAssessmentRepository.findByUserIdAndAssessmentId(userId, assessmentId);
    }

<<<<<<< HEAD
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }

    // Inner class for assessment statistics
    public static class AssessmentStatistics {
        private int totalSubmissions;
        private BigDecimal averageScore;
        private BigDecimal maximumScore;
        private BigDecimal minimumScore;
        private long passedCount;
        private double passRate;

        // Getters and setters
        public int getTotalSubmissions() { 
            return totalSubmissions; 
        }
        
        public void setTotalSubmissions(int totalSubmissions) { 
            this.totalSubmissions = totalSubmissions; 
        }
        
        public BigDecimal getAverageScore() { 
            return averageScore; 
        }
        
        public void setAverageScore(BigDecimal averageScore) { 
            this.averageScore = averageScore; 
        }
        
        public BigDecimal getMaximumScore() { 
            return maximumScore; 
        }
        
        public void setMaximumScore(BigDecimal maximumScore) { 
            this.maximumScore = maximumScore; 
        }
        
        public BigDecimal getMinimumScore() { 
            return minimumScore; 
        }
        
        public void setMinimumScore(BigDecimal minimumScore) { 
            this.minimumScore = minimumScore; 
        }
        
        public long getPassedCount() { 
            return passedCount; 
        }
        
        public void setPassedCount(long passedCount) { 
            this.passedCount = passedCount; 
        }
        
        public double getPassRate() { 
            return passRate; 
        }
        
        public void setPassRate(double passRate) { 
            this.passRate = passRate; 
        }
    }
=======
	public void deleteAssessment(Long id) {
		assessmentRepository.deleteById(id);
	}
>>>>>>> parent of ea94e5f (Assessment)
}