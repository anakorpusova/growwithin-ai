package com.growwithin.backend;

import tools.jackson.databind.JsonNode;
import java.time.Duration;
import java.util.List;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class AiCareerAdviceService {

    private static final String GEMINI_BASE_URL =
            "https://generativelanguage.googleapis.com";

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public AiCareerAdviceService(
            @Value("${ai.gemini.api-key:}") String apiKey,
            @Value("${ai.gemini.model:gemini-3.5-flash-lite}") String model) {

        var httpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        this.restClient = RestClient.builder()
                .baseUrl(GEMINI_BASE_URL)
                .requestFactory(requestFactory)
                .build();
        this.apiKey = apiKey;
        this.model = model;
    }

    public AiAdvice generateAdvice(
            List<EmployeeSkill> employeeSkills,
            List<OpportunityMatch> matches) {

        if (apiKey == null || apiKey.isBlank()) {
            return new AiAdvice(
                    "AI recommendation is not configured. "
                            + "Set GEMINI_API_KEY to enable it.",
                    model,
                    false
            );
        }

        String prompt = buildPrompt(employeeSkills, matches);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        try {
            JsonNode response = restClient.post()
                    .uri("/v1beta/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            String recommendation = response == null
                    ? ""
                    : response.path("candidates")
                            .path(0)
                            .path("content")
                            .path("parts")
                            .path(0)
                            .path("text")
                            .asText("");

            if (recommendation.isBlank()) {
                return new AiAdvice(
                        "The AI service returned an empty recommendation.",
                        model,
                        false
                );
            }

            return new AiAdvice(recommendation.trim(), model, true);

        } catch (RestClientException exception) {
            return new AiAdvice(
                    "The AI recommendation service is temporarily unavailable.",
                    model,
                    false
            );
        }
    }

    private String buildPrompt(
            List<EmployeeSkill> employeeSkills,
            List<OpportunityMatch> matches) {

        String skills = employeeSkills.stream()
                .map(skill -> skill.name()
                        + " (" + skill.verificationStatus() + ")")
                .collect(Collectors.joining(", "));

        String opportunities = matches.stream()
                .map(match -> match.title()
                        + "; matching skills: " + match.matchingSkills()
                        + "; missing skills: " + match.missingSkills()
                        + "; unverified skills: " + match.unverifiedSkills())
                .collect(Collectors.joining("\n"));

        return """
                You are a career development assistant.
                Review the employee skills and opportunity matches below.
                Recommend the best opportunity and the next one or two skills
                the employee should develop.
                Keep the response professional and limited to three sentences.
                Do not invent skills that are not shown in the data.

                Employee skills:
                %s

                Opportunity matches:
                %s
                """.formatted(skills, opportunities);
    }
}
