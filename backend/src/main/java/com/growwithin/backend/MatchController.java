package com.growwithin.backend;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final SkillMatchingService matchingService;
    private final AiCareerAdviceService aiCareerAdviceService;

    public MatchController(
            SkillMatchingService matchingService,
            AiCareerAdviceService aiCareerAdviceService) {

        this.matchingService = matchingService;
        this.aiCareerAdviceService = aiCareerAdviceService;
    }

    @PostMapping
    public List<OpportunityMatch> match(
            @Valid @RequestBody MatchRequest request) {

        return matchingService.match(request.employeeSkills());
    }

    @PostMapping("/with-advice")
    public CareerPlanResponse matchWithAdvice(
            @Valid @RequestBody MatchRequest request) {

        List<OpportunityMatch> matches =
                matchingService.match(request.employeeSkills());

        AiAdvice aiAdvice =
                aiCareerAdviceService.generateAdvice(
                        request.employeeSkills(), matches);

        return new CareerPlanResponse(matches, aiAdvice);
    }
}
