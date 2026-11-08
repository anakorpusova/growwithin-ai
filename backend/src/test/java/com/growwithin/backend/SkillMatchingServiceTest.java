package com.growwithin.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SkillMatchingServiceTest {
    private final SkillMatchingService service = new SkillMatchingService();

    @Test
    void separatesVerifiedUnverifiedAndMissingSkills() {
        var matches = service.match(List.of(
                new EmployeeSkill("Java", EmployeeSkill.VerificationStatus.VERIFIED),
                new EmployeeSkill("Git", EmployeeSkill.VerificationStatus.UNVERIFIED)
        ));

        var result = matches.stream()
                .filter(match -> match.opportunityId().equals("software-engineer"))
                .findFirst()
                .orElseThrow();

        assertEquals(List.of("Java"), result.matchingSkills());
        assertEquals(List.of("REST APIs"), result.missingSkills());
        assertEquals(List.of("Git"), result.unverifiedSkills());
        assertEquals("GROWTH_OPPORTUNITY", result.readiness());
    }
}