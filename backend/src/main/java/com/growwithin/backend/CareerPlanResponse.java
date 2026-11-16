package com.growwithin.backend;

import java.util.List;

public record CareerPlanResponse(
        List<OpportunityMatch> matches,
        AiAdvice aiAdvice
) {}
