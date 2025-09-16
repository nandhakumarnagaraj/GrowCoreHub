package com.growcorehub.service;

import com.growcorehub.dto.AssessmentDto;
import com.growcorehub.entity.Assessment;
import com.growcorehub.entity.UserAssessment;
import com.growcorehub.utils.AssessmentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentTestService {

	@Autowired
	private AssessmentService assessmentService;

	/**
	 * Create sample Java assessment
	 */
	public Assessment createSampleJavaAssessment(Long projectId) {
		String questionsJson = AssessmentUtils.createSampleJavaAssessment();

		AssessmentDto assessmentDto = new AssessmentDto();
		assessmentDto.setProjectId(projectId);
		assessmentDto.setName("Java Developer Skills Assessment");
		assessmentDto.setDescription(
				"Comprehensive assessment covering Java fundamentals, OOP concepts, and practical coding skills");
		assessmentDto.setQuestions(questionsJson);
		assessmentDto.setMaxScore(BigDecimal.valueOf(100));
		assessmentDto.setTimeLimitMinutes(60);

		return assessmentService.createAssessment(assessmentDto);
	}

	/**
	 * Create custom assessment based on type
	 */
	public Assessment createCustomAssessment(Long projectId, String assessmentType) {
		String questionsJson;
		String name;
		String description;

		switch (assessmentType.toLowerCase()) {
		case "python":
			questionsJson = createPythonAssessment();
			name = "Python Developer Assessment";
			description = "Assessment covering Python fundamentals and web development";
			break;
		case "javascript":
			questionsJson = createJavaScriptAssessment();
			name = "JavaScript Developer Assessment";
			description = "Assessment covering JavaScript, ES6+, and web development";
			break;
		case "database":
			questionsJson = createDatabaseAssessment();
			name = "Database Skills Assessment";
			description = "Assessment covering SQL, database design, and optimization";
			break;
		default:
			questionsJson = AssessmentUtils.createSampleJavaAssessment();
			name = "Java Developer Assessment";
			description = "Default Java assessment";
		}

		AssessmentDto assessmentDto = new AssessmentDto();
		assessmentDto.setProjectId(projectId);
		assessmentDto.setName(name);
		assessmentDto.setDescription(description);
		assessmentDto.setQuestions(questionsJson);
		assessmentDto.setMaxScore(BigDecimal.valueOf(100));
		assessmentDto.setTimeLimitMinutes(60);

		return assessmentService.createAssessment(assessmentDto);
	}

	/**
	 * Submit sample answers with predefined quality
	 */
	public UserAssessment submitSampleAnswers(Long userId, Long assessmentId, String answerQuality) {
		String answers;
		BigDecimal score;

		switch (answerQuality.toLowerCase()) {
		case "perfect":
			answers = AssessmentUtils.createSampleAnswers();
			score = BigDecimal.valueOf(95.0);
			break;
		case "good":
			answers = createGoodAnswers();
			score = BigDecimal.valueOf(80.0);
			break;
		case "poor":
			answers = createPoorAnswers();
			score = BigDecimal.valueOf(45.0);
			break;
		default:
			answers = AssessmentUtils.createSampleAnswers();
			score = BigDecimal.valueOf(85.0);
		}

		return assessmentService.submitAssessment(userId, assessmentId, answers, score);
	}

	/**
	 * Validate assessment JSON structure
	 */
	public Map<String, Object> validateQuestionsJson(String questionsJson) {
		Map<String, Object> result = new HashMap<>();
		boolean isValid = AssessmentUtils.isValidAssessmentJson(questionsJson);

		result.put("valid", isValid);

		if (isValid) {
			result.put("questionIds", AssessmentUtils.getQuestionIds(questionsJson));
			result.put("totalPoints", AssessmentUtils.calculateTotalPoints(questionsJson));
		} else {
			result.put("message", "Invalid assessment JSON structure");
		}

		return result;
	}

	/**
	 * Validate answers JSON structure
	 */
	public Map<String, Object> validateAnswersJson(String answersJson) {
		Map<String, Object> result = new HashMap<>();
		boolean isValid = AssessmentUtils.isValidAnswersJson(answersJson);

		result.put("valid", isValid);

		if (!isValid) {
			result.put("message", "Invalid answers JSON structure");
		}

		return result;
	}

	// Helper methods for creating different types of answers
	private String createGoodAnswers() {
		return "{\"q1\":\"Programming Language\",\"q2\":\"extends\",\"q3\":\"true\",\"q4\":\"0\",\"q5\":\"OOP in Java includes inheritance, polymorphism, and encapsulation. Classes can inherit from other classes.\",\"q6\":\"int factorial(int n) { if (n <= 1) return 1; return n * factorial(n-1); }\",\"q7\":\"List\",\"q8\":\"true\",\"q9\":\"127\",\"q10\":\"I have some experience with Java and have worked on small projects.\"}";
	}

	private String createPoorAnswers() {
		return "{\"q1\":\"Database\",\"q2\":\"implements\",\"q3\":\"false\",\"q4\":\"1\",\"q5\":\"OOP is good\",\"q6\":\"factorial\",\"q7\":\"Set\",\"q8\":\"false\",\"q9\":\"128\",\"q10\":\"No experience\"}";
	}

	private String createPythonAssessment() {
		return new AssessmentUtils.AssessmentBuilder("Python Assessment", "Python skills evaluation")
				.addMultipleChoiceQuestion("p1", "What is Python?",
						List.of("Interpreted Language", "Compiled Language", "Database", "Framework"),
						"Interpreted Language", 10)
				.addTrueFalseQuestion("p2", "Python is dynamically typed.", true, 5)
				.addTextQuestion("p3", "Explain list comprehensions in Python",
						List.of("list", "comprehension", "for", "in"), 15)
				.addMultipleChoiceQuestion("p4", "Which keyword is used to define a function in Python?",
						List.of("def", "function", "func", "define"), "def", 10)
				.addTextQuestion("p5", "Write a Python function to reverse a string",
						List.of("reverse", "string", "function", "return"), 20)
				.buildQuestionsJson();
	}

	private String createJavaScriptAssessment() {
		return new AssessmentUtils.AssessmentBuilder("JavaScript Assessment", "JavaScript skills evaluation")
				.addMultipleChoiceQuestion("js1", "What is JavaScript primarily used for?",
						List.of("Web Development", "Desktop Apps", "Mobile Apps", "All of the above"),
						"All of the above", 10)
				.addTrueFalseQuestion("js2", "JavaScript is the same as Java.", false, 5)
				.addTextQuestion("js3", "Explain closures in JavaScript",
						List.of("closure", "scope", "function", "variable"), 20)
				.addMultipleChoiceQuestion("js4", "Which method is used to add an element to the end of an array?",
						List.of("push", "pop", "shift", "unshift"), "push", 10)
				.addTextQuestion("js5", "Write a JavaScript function to check if a number is prime",
						List.of("prime", "number", "function", "return"), 25)
				.buildQuestionsJson();
	}

	private String createDatabaseAssessment() {
		return new AssessmentUtils.AssessmentBuilder("Database Assessment", "SQL and database skills evaluation")
				.addMultipleChoiceQuestion("db1", "What does SQL stand for?",
						List.of("Structured Query Language", "Simple Query Language", "Standard Query Language",
								"System Query Language"),
						"Structured Query Language", 10)
				.addTextQuestion("db2", "Explain the difference between INNER JOIN and LEFT JOIN",
						List.of("inner", "left", "join", "table", "null"), 20)
				.addMultipleChoiceQuestion("db3", "Which constraint ensures unique values in a column?",
						List.of("PRIMARY KEY", "UNIQUE", "NOT NULL", "CHECK"), "UNIQUE", 10)
				.addTextQuestion("db4", "Write a SQL query to find the second highest salary from an employees table",
						List.of("SELECT", "MAX", "salary", "employees", "ORDER BY"), 25)
				.addTrueFalseQuestion("db5", "A foreign key can have NULL values.", true, 5).buildQuestionsJson();
	}
}