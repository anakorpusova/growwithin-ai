package com.growwithin.backend;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record MatchRequest(
        @NotEmpty List<@Valid EmployeeSkill> employeeSkills
) {}
