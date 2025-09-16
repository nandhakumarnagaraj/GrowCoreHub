package com.growcorehub.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssessmentUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Create a standardized question JSON structure
     */
    public static class QuestionBuilder {
        private ObjectNode question;

        public QuestionBuilder(String id, String questionText, String type) {
            question = objectMapper.createObjectNode();
            question.put("id", id);
            question.put("question", questionText);
            question.put("type", type);
        }

        public QuestionBuilder withPoints(int points) {
            question.put("points", points);
            return this;
        }

        public QuestionBuilder withOptions(List<String> options) {
            ArrayNode optionsArray = objectMapper.createArrayNode();
            for (String option : options) {
                optionsArray.add(option);
            }
            question.set("options", optionsArray);
            return this;
        }

        public QuestionBuilder withCorrectAnswer(String correctAnswer) {
            question.put("correctAnswer", correctAnswer);
            return this;
        }

        public QuestionBuilder withCorrectAnswer(double correctAnswer) {
            question.put("correctAnswer", correctAnswer);
            return this;
        }

        public QuestionBuilder withCorrectAnswer(boolean correctAnswer) {
            question.put("correctAnswer", correctAnswer);
            return this;
        }

        public QuestionBuilder withKeywords(List<String> keywords) {
            ArrayNode keywordsArray = objectMapper.createArrayNode();
            for (String keyword : keywords) {
                keywordsArray.add(keyword);
            }
            question.set("keywords", keywordsArray);
            return this;
        }

        public QuestionBuilder withTolerance(double tolerance) {
            question.put("tolerance", tolerance);
            return this;
        }

        public QuestionBuilder withDescription(String description) {
            question.put("description", description);
            return this;
        }

        public ObjectNode build() {
            return question;
        }
    }

    /**
     * Assessment Builder for creating complete assessments
     */
    public static class AssessmentBuilder {
        private List<ObjectNode> questions;
        private String assessmentName;
        private String description;

        public AssessmentBuilder(String name, String description) {
            this.assessmentName = name;
            this.description = description;
            this.questions = new ArrayList<>();
        }

        public AssessmentBuilder addQuestion(ObjectNode question) {
            questions.add(question);
            return this;
        }

        public AssessmentBuilder addMultipleChoiceQuestion(String id, String question, List<String> options,
                String correctAnswer, int points) {
            ObjectNode q = new QuestionBuilder(id, question, "multiple_choice")
                    .withOptions(options)
                    .withCorrectAnswer(correctAnswer)
                    .withPoints(points)
                    .build();
            questions.add(q);
            return this;
        }

        public AssessmentBuilder addTrueFalseQuestion(String id, String question, boolean correctAnswer, int points) {
            ObjectNode q = new QuestionBuilder(id, question, "true_false")
                    .withCorrectAnswer(correctAnswer)
                    .withPoints(points)
                    .build();
            questions.add(q);
            return this;
        }

        public AssessmentBuilder addNumericalQuestion(String id, String question, double correctAnswer,
                double tolerance, int points) {
            ObjectNode q = new QuestionBuilder(id, question, "numerical")
                    .withCorrectAnswer(correctAnswer)
                    .withTolerance(tolerance)
                    .withPoints(points)
                    .build();
            questions.add(q);
            return this;
        }

        public AssessmentBuilder addTextQuestion(String id, String question, List<String> keywords, int points) {
            ObjectNode q = new QuestionBuilder(id, question, "text")
                    .withKeywords(keywords)
                    .withPoints(points)
                    .build();
            questions.add(q);
            return this;
        }

        public AssessmentBuilder addEssayQuestion(String id, String question, int points) {
            ObjectNode q = new QuestionBuilder(id, question, "text")
                    .withPoints(points)
                    .build();
            questions.add(q);
            return this;
        }

        public String buildQuestionsJson() {
            try {
                ArrayNode questionsArray = objectMapper.createArrayNode();
                for (ObjectNode question : questions) {
                    questionsArray.add(question);
                }
                return objectMapper.writeValueAsString(questionsArray);
            } catch (Exception e) {
                throw new RuntimeException("Error building questions JSON", e);
            }
        }

        public int getTotalPoints() {
            return questions.stream().mapToInt(q -> q.get("points").asInt()).sum();
        }
    }

    /**
     * Validate assessment JSON structure
     */
    public static boolean isValidAssessmentJson(String questionsJson) {
        try {
            JsonNode node = objectMapper.readTree(questionsJson);
            if (!node.isArray()) {
                return false;
            }

            for (JsonNode question : node) {
                if (!question.has("id") || !question.has("question") || !question.has("type")
                        || !question.has("points")) {
                    return false;
                }

                String type = question.get("type").asText();
                switch (type) {
                case "multiple_choice":
                    if (!question.has("options") || !question.has("correctAnswer")) {
                        return false;
                    }
                    break;
                case "true_false":
                    if (!question.has("correctAnswer")) {
                        return false;
                    }
                    break;
                case "numerical":
                    if (!question.has("correctAnswer")) {
                        return false;
                    }
                    break;
                case "text":
                    // Text questions are valid with or without keywords
                    break;
                default:
                    return false; // Unknown question type
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate answers JSON structure
     */
    public static boolean isValidAnswersJson(String answersJson) {
        try {
            JsonNode node = objectMapper.readTree(answersJson);
            return node.isObject() && node.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get question IDs from assessment JSON
     */
    public static List<String> getQuestionIds(String questionsJson) {
        List<String> ids = new ArrayList<>();
        try {
            JsonNode node = objectMapper.readTree(questionsJson);
            if (node.isArray()) {
                for (JsonNode question : node) {
                    if (question.has("id")) {
                        ids.add(question.get("id").asText());
                    }
                }
            }
        } catch (Exception e) {
            // Return empty list on error
        }
        return ids;
    }

    /**
     * Calculate total possible points from assessment
     */
    public static int calculateTotalPoints(String questionsJson) {
        int total = 0;
        try {
            JsonNode node = objectMapper.readTree(questionsJson);
            if (node.isArray()) {
                for (JsonNode question : node) {
                    if (question.has("points")) {
                        total += question.get("points").asInt();
                    }
                }
            }
        } catch (Exception e) {
            // Return 0 on error
        }
        return total;
    }

    /**
     * Create a sample Java programming assessment
     */
    public static String createSampleJavaAssessment() {
        List<String> javaBasicsOptions = List.of("Programming Language", "Database", "Framework", "Operating System");
        List<String> inheritanceOptions = List.of("extends", "implements", "inherits", "super");
        List<String> collectionOptions = List.of("Set", "List", "Map", "Queue");
        List<String> oopKeywords = List.of("inheritance", "polymorphism", "encapsulation", "abstraction");
        List<String> codingKeywords = List.of("factorial", "recursive", "loop", "method", "return");

        return new AssessmentBuilder("Java Developer Assessment", "Comprehensive Java programming skills assessment")
                .addMultipleChoiceQuestion("q1", "What is Java?", javaBasicsOptions, "Programming Language", 10)
                .addMultipleChoiceQuestion("q2", "Which keyword is used to inherit a class in Java?",
                        inheritanceOptions, "extends", 10)
                .addTrueFalseQuestion("q3", "Java is platform independent.", true, 5)
                .addNumericalQuestion("q4", "What is the default value of int in Java?", 0, 0, 5)
                .addTextQuestion("q5",
                        "Explain the concept of Object-Oriented Programming in Java. Mention at least 3 key principles.",
                        oopKeywords, 20)
                .addTextQuestion("q6", "Write a simple Java method to calculate the factorial of a number.",
                        codingKeywords, 25)
                .addMultipleChoiceQuestion("q7", "Which collection class allows duplicate elements?", collectionOptions,
                        "List", 10)
                .addTrueFalseQuestion("q8", "Java supports multiple inheritance through interfaces.", true, 5)
                .addNumericalQuestion("q9", "What is the maximum value of byte in Java?", 127, 0, 5)
                .addEssayQuestion("q10",
                        "Describe your experience with Java development and any projects you've worked on.", 5)
                .buildQuestionsJson();
    }

    /**
     * Create a sample answers JSON for testing
     */
    public static String createSampleAnswers() {
        ObjectNode answers = objectMapper.createObjectNode();
        answers.put("q1", "Programming Language");
        answers.put("q2", "extends");
        answers.put("q3", "true");
        answers.put("q4", "0");
        answers.put("q5",
                "Object-Oriented Programming in Java includes four main principles: inheritance allows classes to inherit properties from parent classes, polymorphism enables objects to take multiple forms, encapsulation hides internal implementation details, and abstraction provides simplified interfaces.");
        answers.put("q6", "public int factorial(int n) { if (n <= 1) return 1; else return n * factorial(n-1); }");
        answers.put("q7", "List");
        answers.put("q8", "true");
        answers.put("q9", "127");
        answers.put("q10",
                "I have 3 years of experience with Java development. I've worked on several projects including web applications using Spring Boot, RESTful APIs, and database integration with Hibernate.");

        try {
            return objectMapper.writeValueAsString(answers);
        } catch (Exception e) {
            throw new RuntimeException("Error creating sample answers", e);
        }
    }
}