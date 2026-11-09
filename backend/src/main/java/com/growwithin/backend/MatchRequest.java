package com.growwithin.backend;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MatchRequest(
        @NotEmpty List<@NotNull @Valid EmployeeSkill> employeeSkills
) {}