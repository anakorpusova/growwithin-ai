package com.growwithin.backend;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final SkillMatchingService matchingService;

    public MatchController(SkillMatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping
    public List<OpportunityMatch> match(
            @Valid @RequestBody MatchRequest request) {
        return matchingService.match(request.employeeSkills());
    }
}