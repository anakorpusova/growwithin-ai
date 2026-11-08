package com.growwithin.backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SkillMatchingService {
    private final List<Opportunity> opportunities = List.of(
            new Opportunity("software-engineer", "Software Engineer",
                    List.of("Java", "Git", "REST APIs")),
            new Opportunity("data-analyst", "Data Analyst",
                    List.of("SQL", "Python", "Data Visualization")),
            new Opportunity("project-coordinator", "Project Coordinator",
                    List.of("Communication", "Agile", "Documentation"))
    );

    public List<OpportunityMatch> match(List<EmployeeSkill> employeeSkills) {
        Map<String, EmployeeSkill.VerificationStatus> submitted = new HashMap<>();

        for (EmployeeSkill skill : employeeSkills) {
            submitted.put(normalize(skill.name()), skill.verificationStatus());
        }

        return opportunities.stream()
                .map(opportunity -> createMatch(opportunity, submitted))
                .sorted(Comparator.comparingInt(
                        (OpportunityMatch match) -> match.matchingSkills().size()
                ).reversed())
                .limit(3)
                .toList();
    }

    private OpportunityMatch createMatch(
            Opportunity opportunity,
            Map<String, EmployeeSkill.VerificationStatus> submitted) {

        List<String> matching = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        List<String> unverified = new ArrayList<>();

        for (String requiredSkill : opportunity.requiredSkills()) {
            var status = submitted.get(normalize(requiredSkill));

            if (status == EmployeeSkill.VerificationStatus.VERIFIED) {
                matching.add(requiredSkill);
            } else if (status == EmployeeSkill.VerificationStatus.UNVERIFIED) {
                unverified.add(requiredSkill);
            } else {
                missing.add(requiredSkill);
            }
        }

        String readiness = missing.isEmpty() && unverified.isEmpty()
                ? "READY_NOW"
                : "GROWTH_OPPORTUNITY";

        return new OpportunityMatch(
                opportunity.id(), opportunity.title(), readiness,
                matching, missing, unverified
        );
    }

    private String normalize(String skill) {
        return skill.trim().toLowerCase();
    }

    private record Opportunity(
            String id,
            String title,
            List<String> requiredSkills
    ) {}
}