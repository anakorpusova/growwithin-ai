package com.growwithin.backend;

public record AiAdvice(
        String recommendation,
        String model,
        boolean aiGenerated
) {}
