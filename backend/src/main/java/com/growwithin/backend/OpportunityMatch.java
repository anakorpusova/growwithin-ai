package com.growwithin.backend;

import java.util.List;

public record OpportunityMatch(
        String opportunityId,
        String title,
        String readiness,
        List<String> matchingSkills,
        List<String> missingSkills,
        List<String> unverifiedSkills
) {}