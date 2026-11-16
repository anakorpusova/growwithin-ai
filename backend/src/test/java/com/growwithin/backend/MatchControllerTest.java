package com.growwithin.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "ai.gemini.api-key=")
@AutoConfigureMockMvc
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rejectsNullEmployeeSkill() throws Exception {
        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeSkills\":[null]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsMatchesAndSafeAiFallbackWithoutApiKey() throws Exception {
        mockMvc.perform(post("/api/matches/with-advice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "employeeSkills": [
                                    {
                                      "name": "Java",
                                      "verificationStatus": "VERIFIED"
                                    },
                                    {
                                      "name": "Git",
                                      "verificationStatus": "UNVERIFIED"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches[0].opportunityId")
                        .value("software-engineer"))
                .andExpect(jsonPath("$.matches[0].matchingSkills[0]")
                        .value("Java"))
                .andExpect(jsonPath("$.aiAdvice.aiGenerated")
                        .value(false))
                .andExpect(jsonPath("$.aiAdvice.model")
                        .value("gemini-3.5-flash-lite"));
    }
}
